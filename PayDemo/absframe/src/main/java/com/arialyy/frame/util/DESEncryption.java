package com.arialyy.frame.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


import android.content.Context;
import android.util.Base64;

import com.arialyy.frame.config.Constance;


/***
 * DES文件加密&解密 <br>
 * 可以实现android和window的文件互通
 *
 *
 */
public class DESEncryption {
    // a weak key

    private static String encoding = "UTF-8";
    // 密钥
    private static String sKey = "12345678";

    public DESEncryption(String sKey) {
        this.sKey = sKey;
    }

    static byte[] key = new byte[]{1,2,3,4,5,6,7,8};

    /**
     * 对称加密字节数组并返回
     *
     * @param byteSource
     *            需要加密的数据
     * @return 经过加密的数据
     * @throws Exception
     */
    static byte[] iv = new byte[]{1,2,3,4,5,6,7,8};
    public static byte[] encryptByte(byte[] byteSource) throws Exception {
        if (byteSource.length % 8 != 0) { // not a multiple of 8
            // create a new array with a size which is a multiple of 8
            byte[] padded = new byte[byteSource.length + 8
                    - (byteSource.length % 8)];

            // copy the old array into it
            System.arraycopy(byteSource, 0, padded, 0, byteSource.length);
            byteSource = padded;
        }
        int mode = Cipher.ENCRYPT_MODE;
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//		byte[] keyData = sKey.getBytes();
        DESKeySpec keySpec = new DESKeySpec(key);
        Key key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(mode, key, ips);
        // 把byte加密后进行BASE64编码返回
        return Base64.encode(cipher.doFinal(byteSource), Base64.DEFAULT);
    }

    /**
     * 对称解密字节数组并返回
     *
     * @param byteSource
     *            需要解密的数据
     * @return 经过解密的数据
     * @throws Exception
     */
    public static byte[] decryptByte(byte[] byteSource) throws Exception {
//		if (byteSource.length % 8 != 0) { // not a multiple of 8
//			// create a new array with a size which is a multiple of 8
//			byte[] padded = new byte[byteSource.length + 8
//					- (byteSource.length % 8)];
//			// copy the old array into it
//			System.arraycopy(byteSource, 0, padded, 0, byteSource.length);
//			byteSource = padded;
//		}

        int mode = Cipher.DECRYPT_MODE;
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//		byte[] keyData = sKey.getBytes();
        DESKeySpec keySpec = new DESKeySpec(key);
        Key key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv);
        cipher.init(mode, key, ips);
        // 先进行BASE64后在解密byte
        byteSource = Base64.decode(byteSource, Base64.DEFAULT);
        return cipher.doFinal(byteSource);
    }


    /**
     * des加密文件
     *
     * @param srcFile
     *            源文件路径
     * @param distFile
     *            解密后的文件路径
     */
    int recordLen = 0;
    public void BASE64EncoderFile(Context context, String srcFile, String distFile) {
        InputStream inputStream = null;
        OutputStream out = null;
        int len = 0;
        try {
            inputStream = new FileInputStream(srcFile);
            out = new FileOutputStream(distFile);
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                byte[] temp = new byte[len];
                System.arraycopy(buffer, 0, temp, 0, len);
                byte[] result = encryptByte(temp);
                //！！！记录写入的长度，以便解密时能获取相应的块长度！！必须这样否则会出问题
                //只记录最长的
                if(recordLen == 0){
                    recordLen = result.length;
                    //把长度记录到配置文件里面
                    SharePreUtil.putInt(Constance.Encryption.SHARE_NAME, context, Constance.Encryption.DES_ENCRTPT_LEN, result.length);
                }
                out.write(result, 0, result.length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DES解密
     *
     * @param srcFile
     *            源文件路径
     * @param distFile
     *            解密后的文件路径
     */
    public void BASE64DecoderFile(Context context, String srcFile, String distFile) {
        InputStream inputStream = null;
        OutputStream out = null;
        int len = 0;
        try {
            inputStream = new FileInputStream(srcFile);
            out = new FileOutputStream(distFile);
            //！！长度为实际写的长度，这样读的时候就不会读少，才不会解密错误，读取的长度必须为加密的长度
            //从配置文件读取长度
            if(recordLen == 0){
                recordLen = SharePreUtil.getInt(Constance.Encryption.SHARE_NAME, context, Constance.Encryption.DES_ENCRTPT_LEN);
            }
            byte[] buffer = new byte[recordLen];
            while ((len = inputStream.read(buffer)) > 0) {
                byte[] temp = new byte[len];
                System.arraycopy(buffer, 0, temp, 0, len);
                byte[] result = decryptByte(temp);
                out.write(result, 0, result.length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}