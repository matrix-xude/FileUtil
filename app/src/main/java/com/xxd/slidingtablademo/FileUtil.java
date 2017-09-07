package com.xxd.slidingtablademo;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by xxd on 2017/9/7.
 * 操作文件的工具类
 */

public class FileUtil {

    /**
     * 基本文件操作，默认编码方式
     */
    public static String FILE_READING_ENCODING = "UTF-8";
    public static String FILE_WRITING_ENCODING = "UTF-8";

    /**
     * 创建文件夹
     *
     * @param path
     */
    public static void createFolder(String path) {
        File file = new File(path);
        createFolder(file);
    }

    /**
     * 创建文件夹
     *
     * @param file
     */
    public static void createFolder(File file) {
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            Log.e("Tag",mkdirs+"");
        }
    }

    /**
     * 创建新文件
     *
     * @param path
     * @param fileName
     * @return
     */
    public static File createFile(String path, String fileName, boolean deleteExist) {
        File file = new File(path, fileName);
        return createFile(file, deleteExist);
    }

    /**
     * 创建新文件
     *
     * @param path
     * @return
     */
    public static File createFile(String path, boolean deleteExist) {
        File file = new File(path);
        return createFile(file, deleteExist);
    }

    /**
     * 创建一个新的文件，如果之前有，删除之前的
     *
     * @return
     */
    public static File createFile(File file, boolean deleteExist) {
        try {
            if (!file.exists()) {
                createFolder(file.getParentFile());
                file.createNewFile();
            } else {
                if (deleteExist) {
                    file.delete();
                    file.createNewFile();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 读取文件为String类型
     *
     * @param fileName
     * @param encoding
     * @return
     * @throws Exception
     */
    public static String readFile(String fileName, String encoding) throws Exception {
        StringBuffer buffContent = null;
        String sLine;

        FileInputStream fis = null;
        BufferedReader buffReader = null;
        if (encoding == null || "".equals(encoding)) {
            encoding = FILE_READING_ENCODING;
        }

        try {
            fis = new FileInputStream(fileName);
            buffReader = new BufferedReader(new InputStreamReader(fis, encoding));
            while ((sLine = buffReader.readLine()) != null) {
                if (buffContent == null) {
                    buffContent = new StringBuffer();
                } else {
                    buffContent.append("\n");
                }
                buffContent.append(sLine);
            }
            return (buffContent == null ? "" : buffContent.toString());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new Exception("要读取的文件没有找到!", ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new Exception("读取文件时错误!", ex);
        } finally {
            // 增加异常时资源的释放
            try {
                if (buffReader != null)
                    buffReader.close();
                if (fis != null)
                    fis.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 写文件
    public static File writeFile(String path, String content) throws Exception {
        InputStream is = new ByteArrayInputStream(content.getBytes(FILE_WRITING_ENCODING));
        return writeFile(is, path, false);
    }


    // 写文件
    public static File writeFile(String path, String content, String encoding) throws Exception {
        if (TextUtils.isEmpty(encoding)) {
            encoding = FILE_WRITING_ENCODING;
        }
        InputStream is = new ByteArrayInputStream(content.getBytes(encoding));
        return writeFile(is, path, false);
    }

    // 写文件
    public static File writeFile(String path, String content, String encoding, boolean append) throws Exception {
        if (TextUtils.isEmpty(encoding)) {
            encoding = FILE_WRITING_ENCODING;
        }
        InputStream is = new ByteArrayInputStream(content.getBytes(encoding));
        return writeFile(is, path, append);
    }

    /**
     * 写is到某个文件中
     *
     * @param is
     * @param path
     * @param append 如果文件已存在，是否在最后添加
     * @return
     * @throws Exception
     */
    public static File writeFile(InputStream is, String path, boolean append) throws Exception {

        File file = createFile(path, !append);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file, append);
            int byteCount;
            byte[] bytes = new byte[1024];

            while ((byteCount = is.read(bytes)) != -1) {
                os.write(bytes, 0, byteCount);
            }
            os.flush();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("写文件错误", e);
        } finally {
            try {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件，及其所有子目录文件
     *
     * @param path
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return deleteFile(file);
    }

    /**
     * 删除文件，及其所有子目录文件
     *
     * @param file
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) { // 是文件夹，遍历删除
            File[] files = file.listFiles();
            int length = files.length;
            for (int i = 0; i < length; i++) {
                if (files[i].isDirectory()) {
                    if (!deleteFile(files[i])) {
                        return false;
                    }
                } else {
                    if (!files[i].delete()) {
                        return false;
                    }
                }
            }
            if (!file.delete()) {
                return false;
            }
        } else {  // 是文件，直接删除
            return file.delete();
        }
        return true;
    }


    public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {

                File file = new File(outPathString + File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

}
