package com.arialyy.frame.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Parcelable;
import android.util.Log;

import com.arialyy.frame.util.show.FL;
import com.arialyy.frame.util.show.L;

/**
 * IC卡操作工具类
 *
 * @author Administrator
 */
@SuppressLint("NewApi")
public class ICCardUtil {
    private static final String TAG = "ICCardUtil";

    /**
     * 获取标签的卡片类型
     *
     * @param intent
     * @return 返回标签的所具有的格式
     */
    public static String[] getTagType(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] techList = tag.getTechList();
        return techList;
    }

    /**
     * 创建NEDF text的过滤器
     *
     * @return
     */
    @SuppressLint("InlinedApi")
    public static IntentFilter getNdefTextFilter() {
        IntentFilter ndefIntentFilter = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntentFilter.addDataType("text/plain");
        } catch (MalformedMimeTypeException e) {
            Log.e(TAG, "ndefFilter添加格式出错", e);
        }
        return ndefIntentFilter;
    }

    /**
     * 创建泛型过滤器
     *
     * @return
     */
    @SuppressLint("InlinedApi")
    public static IntentFilter getTagFilter() {
        IntentFilter tag = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        return tag;
    }

    /**
     * 创建tech标签过滤器组
     *
     * @return
     */
    public static String[][] getTechArrayList() {
        String[][] mTechLists = new String[][]{
                new String[]{NfcA.class.getName(),// 我的我的标签
                        Ndef.class.getName(), MifareUltralight.class.getName()},
                new String[]{NfcA.class.getName(), Ndef.class.getName(),
                        MifareClassic.class.getName()},
                new String[]{
                        NfcA.class.getName(), // 项目标签
                        MifareClassic.class.getName(),
                        NdefFormatable.class.getName()}};
        return mTechLists;
    }

    /**
     * 创建文本类型的NFC Record
     *
     * @param payload 需要写入的内容
     * @return Record 转换为IC卡可存储的格式
     */
    public static NdefRecord createTextRecord(String payload) {
        // 把字符串转换成utf-8字符码
        Charset utfEncoding = Charset.forName("UTF-8");
        byte[] textBytes = payload.getBytes(utfEncoding);
        NdefRecord ndefRecord = null;
        // TNF和TYPE这两个参数非常的重要，直接决定了你NDEF格式的命中程度。D
        // 不经常使用.....是用来唯一标示一个record的。
        // payload这个才是真正的数据区域。你的读写操作一般应该主要是对这个区域的操作。
        ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], textBytes);
        return ndefRecord;
    }

    /**
     * 创建一组数据
     *
     * @param payloads
     * @return
     */
    public static NdefRecord[] createTextRecord(String[] payloads) {
        NdefRecord[] records = new NdefRecord[payloads.length];
        int i = 0;
        for (String str : payloads) {
            records[i] = createTextRecord(str);
            i++;
        }
        return records;
    }

    /**
     * 创建文本类型的record
     *
     * @param payload
     * @param locale       地区语言
     * @param encodeInUtf8 是否使用utf8
     * @return
     */
    public static NdefRecord createRecord(String payload, Locale locale,
                                          boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(
                Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
                .forName("UTF-16");
        // 把字符串转换成字符码
        byte[] textBytes = payload.getBytes(utfEncoding);
        // 进行翻译转换地区语言
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;

        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
                textBytes.length);
        NdefRecord ndefRecord = null;
        // TNF和TYPE这两个参数非常的重要，直接决定了你NDEF格式的命中程度。D
        // 不经常使用.....是用来唯一标示一个record的。
        // payload这个才是真正的数据区域。你的读写操作一般应该主要是对这个区域的操作。
        ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }

    /**
     * 创建uri的record
     *
     * @param uri
     * @return
     */
    public static NdefRecord creatUriRecord(String uri) {
        // 把字符串转换成utf-8字符码
        Charset utfEncoding = Charset.forName("UTF-8");
        byte[] textBytes = uri.getBytes(utfEncoding);
        NdefRecord ndefRecord = null;
        // TNF和TYPE这两个参数非常的重要，直接决定了你NDEF格式的命中程度。D
        // 不经常使用.....是用来唯一标示一个record的。
        // payload这个才是真正的数据区域。你的读写操作一般应该主要是对这个区域的操作。
        ndefRecord = new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI,
                NdefRecord.RTD_TEXT, new byte[0], textBytes);
        return ndefRecord;
    }

    /**
     * 给标签写入信息
     *
     * @param intent  连接到IC卡时所获取到的intent
     * @param records 写入的数据
     */
    public static boolean writeNdefTag(Intent intent, NdefRecord[] records) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); // 取出intent封装的TAG
        if (tag == null) {
            L.e(TAG, "TAGNULL");
            return false;
        }
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {
            L.e(TAG, "NDEFNULL");
            return false;
        }
        try {
            ndef.close();
            ndef.connect();
            NdefMessage msg = new NdefMessage(records);
            ndef.writeNdefMessage(msg);
            return true;
        } catch (IOException e) {
            FL.e(TAG, "和标签连接失败\n" + FL.getPrintException(e));
        } catch (FormatException e) {
            FL.e(TAG, "写入数据失败\n" + FL.getPrintException(e));
        }
        return false;
    }

    /**
     * 把非NDEF的标签转换为NDEF格式的标签
     *
     * @param intent 连接到IC卡时所获取到的intent
     * @return
     */
    public static boolean formatTechTag(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NdefFormatable format = NdefFormatable.get(tag);
        // 只有非NDEF格式的标签才会调用
        if (format != null) {
            try {
                format.connect();
                NdefMessage msg = new NdefMessage(createTextRecord(""));
                format.format(msg);
                return true;
            } catch (IOException | FormatException e) {
                FL.e(TAG, "格式化失败\n" + FL.getPrintException(e));
            }
        }
        return false;
    }

    /**
     * 对TECH写入数据,可加密的,如果出现Transceive failed错误，需要把卡片格式成
     * NDEF格式，然后再格式化回来就可以用了,注意：MifareClassic这种类型的 Sector 0 即第一个扇区是不能写东西的
     *
     * @param intent  NFC封装信息
     * @param payload 要写入的数据
     * @return
     */
    public static boolean writeTechTag(Intent intent, String[] payload) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mc = MifareClassic.get(tag);
        // 把字符串转换成utf-8字符码
        Charset utfEncoding = Charset.forName("UTF-8");
        byte[][] data = new byte[64][];
        int bIndex = 0; // 记录块的编号
        int bCount = 0; // 记录每个扇区的块数
        int sectorCount = mc.getSectorCount(); // 获取标签扇区数
        boolean auth = false;
        // int dataLenght = data.length; //记录data长度
        int currentPosition = 0; // 当前读取到的data的长度

        for (int i = 0; i < payload.length; i++) {
            // 每一条记录,必须16位长度，不够补0
            payload[i] = StringUtil.addStrLenTo16(payload[i]);
            data[i] = payload[i].getBytes(utfEncoding);
        }

        try {
            mc.close();
            mc.connect();

            for (int i = 1; i < sectorCount; i++) {
                // 尝试获取每个sector的权限，只有通过权限认证才能读取数据
                auth = mc.authenticateSectorWithKeyB(i,
                        MifareClassic.KEY_DEFAULT)
                        || mc.authenticateSectorWithKeyA(bIndex,
                        MifareClassic.KEY_DEFAULT);
                if (auth) {
                    Log.d(TAG, "扇区" + i + "验证成功");
                    bCount = mc.getBlockCountInSector(i);
                    bIndex = mc.sectorToBlock(i);

                    // 写入数据,第四个块不能写，因为第四个块是存储key的
                    // Log.i(TAG, "dataLenght:" + dataLenght);
                    // Log.i(TAG, "bIndex" + bIndex);
                    /**
                     * 写数据时还存在问题
                     */
                    for (int j = 0; j < bCount - 1; j++) {

                        if (data[currentPosition] == null) {
                            break;
                        }
                        mc.writeBlock(bIndex, data[currentPosition]);
                        bIndex++;
                        currentPosition++;
                    }
                }
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "写入失败", e);
        } finally {
            try {
                mc.close();
            } catch (IOException e) {
                Log.e(TAG, "关闭连接失败", e);
            }
        }
        return false;
    }

    /**
     * 读取ndef卡片的内容
     *
     * @param intent
     * @return
     */
    public static String[] readNdefCardContent(Intent intent) {
        // 获取标签里面的所有记录
        Parcelable[] rawMessage = intent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        byte[][] data = null; // 返回的字节码
        String[] message = null;
        // 获取每条记录里面的内容
        // actual这个才是真正的数据区域。你的读写操作一般应该主要是对这个区域的操作。
        // 一个NdefMessage可以有多个NdefRecord所以你不要假设所有的数据都仅仅存储在第一个NdefRecord中。
        /* 好像只有一个NdefMessage */
        for (int i = 0; i < rawMessage.length; i++) {
            NdefMessage Ndefmsg = (NdefMessage) rawMessage[i];
            NdefRecord Ndefrecord[] = Ndefmsg.getRecords();
            data = new byte[Ndefrecord.length][];
            for (int j = 0; j < Ndefrecord.length; j++) {
                data[j] = Ndefrecord[j].getPayload();
            }
        }

        message = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                try {
                    message[i] = new String(data[i], "utf-8");
//					Log.i(TAG, "接收到的数据为:" + str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return message;
    }

    /**
     * 读取Tech卡片的内容
     *
     * @param intent
     * @return
     */
    public static String[] readTechCardContent(Intent intent) {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mc = MifareClassic.get(tagFromIntent);
//		int type = mc.getType();// 获取TAG的类型(MifareClassic。。。)
        String[] message = null;    //返回的字符串

		/* 以下代码是读取mifareClassic格式的数据 */
        int bCount = 0; // 记录每个扇区的块数
        int bIndex = 0; // 记录块的编号
        byte[][] data = null;
        int currentPosition = 4;    //记录当前data的长度

        try {
            mc.connect();
            int sectorCount = mc.getSectorCount(); // 获取标签扇区数
            Log.d(TAG, "该标签含有" + sectorCount + "个扇区");
            data = new byte[64][];
            for (int i = 1; i < sectorCount; i++) {
                // 在MifareClassic中，如果你要读取数据，那么必须要有这个数据地址所在的sector的权限，这个权限就是这个sector的trailer的KEY
                // A或KEY B。
                // 对于所有基于MifareClassic的卡来说，每个区最后一个块（block）叫Trailer，16个byte，主要来存放读写该区的key，可以有A，B两个KEY，每个key长6byte，默认的key一般是FF
                // 或 0，每个扇区的内存结构如下：
                // Block 0 Data 16bytes
                // Block 1 Data 16 bytes
                // Block 2 Data 16 bytes
                // Block 3 Trailer 16 bytes
                // 所以在写卡的时候一般不能写最后一个块
                // 尝试获取每个sector的权限，只有通过权限认证才能读取数据
                boolean auth = mc.authenticateSectorWithKeyB(i,
                        MifareClassic.KEY_DEFAULT)
                        || mc.authenticateSectorWithKeyA(bIndex,
                        MifareClassic.KEY_DEFAULT);
                if (auth) {
                    Log.d(TAG, "扇区" + i + "验证成功");
                    bCount = mc.getBlockCountInSector(i);
                    bIndex = mc.sectorToBlock(i);// 得到sector中的block编号
                    // 读取每一个块的数据
                    try {
                        for (int j = 0; j < bCount - 1; j++) {
                            byte[] data1 = mc.readBlock(bIndex);
                            data[currentPosition] = data1;
//							String str = new String(data1, "utf-8");
                            //	Log.i(TAG, "扇区" + i + "的块" + j + "的数据是:" + str);
//							Log.i(TAG, "内容为:" + str);
                            currentPosition++;
                            bIndex++;
                        }
//						Log.d(TAG, "index" + bIndex);
                    } catch (IOException e) {
                        Log.e(TAG, "读取数据失败", e);
                    }
                } else {
                    Log.i(TAG, "没权限访问" + i + "扇区");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "和NFC标签通讯失败", e);
        } finally {
            try {
                mc.close();
            } catch (IOException e) {
                Log.e(TAG, "nfc关闭失败", e);
            }
        }

        message = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                try {
                    message[i] = new String(data[i], "utf-8");
//					Log.i(TAG, "接收到的数据为:" + str);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return message;
    }
}
