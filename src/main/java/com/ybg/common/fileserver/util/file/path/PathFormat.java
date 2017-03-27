package com.ybg.common.fileserver.util.file.path;

import com.ybg.common.fileserver.util.Coder;
import com.ybg.common.fileserver.util.file.IDGenerator;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 类       名:     路径格式
 */
public class PathFormat {

    /**
     * 文件夹 片段
     */
    private String folder;

    /**
     * 日期 片段
     */
    private String date;

    /**
     * UUID 片段
     */
    private String UUID;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 扩展名 片段
     */
    private String extension;

    /**
     * 获取文件夹 片段
     *
     * @return 路径 片段
     */
    public String getFolder() {
        return folder;
    }

    /**
     * 设置文件夹 片段
     *
     * @param folderFragment 片段
     */
    public void setFolder(PathFragment folderFragment) {
        if (folderFragment.getSearchIndex() != -1)
            this.folder = folderFragment.getFragment();
    }

    /**
     * 获取日期 片段
     *
     * @return 片段
     */
    public String getDate() {
        return date;
    }

    /**
     * 设置日期 片段
     *
     * @param folderFragment 片段
     * @param dateFragment 片段
     */
    public void setDate(PathFragment folderFragment, PathFragment dateFragment) {
        if(dateFragment.getSearchIndex() != -1 &&
                (dateFragment.getSearchIndex() - folderFragment.getSearchIndex()) - 1 == IDGenerator.LENGTH_DATE)
            this.date = dateFragment.getFragment();
    }

    /**
     * 获取UUID 片段
     *
     * @return 片段
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * 设置UUID 片段
     *
     * @param dateFragment 片段
     * @param uuidFragment 片段
     */
    public void setUUID(PathFragment dateFragment, PathFragment uuidFragment) {
        if (uuidFragment.getSearchIndex() != -1 &&
                (uuidFragment.getSearchIndex() - dateFragment.getSearchIndex()) - 1 == IDGenerator.LENGTH_UUID)
            this.UUID = uuidFragment.getFragment();
    }

    /**
     * 获取文件名称
     * @return 文件名称
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 获取完整文件名
     * @return 完整文件名
     */
    public String getFullFileName() {
        return getFileName() + "." + getExtension();
    }

    /**
     * 获取编码后的文件全称
     * @return 编码后的文件全称
     */
    public String getEncodedFileName() throws UnsupportedEncodingException {
        try {
            return URLEncoder.encode(getFileName(), Coder.STR_ENCODING)
                    + "." + getExtension();
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException("不支持的编码字符！");
        }
    }

    /**
     * 设置文件名称
     * @param uuidFragment 片段
     * @param fileNameFragment 文件名称
     */
    public void setFileName(PathFragment uuidFragment, PathFragment fileNameFragment) {
        if (fileNameFragment.getSearchIndex() != -1 &&
                uuidFragment.getSearchIndex() < fileNameFragment.getSearchIndex())
            this.fileName = fileNameFragment.getFragment();
    }

    /**
     * 获取扩展名 片段
     *
     * @return 片段
     */
    public String getExtension() {
        return extension;
    }

    /**
     * 设置扩展名 片段
     *
     * @param fileNameFragment 片段
     * @param extensionFragment 片段
     */
    public void setExtension(PathFragment fileNameFragment, PathFragment extensionFragment) {
        if (extensionFragment.getSearchIndex() != -1 &&
                fileNameFragment.getSearchIndex() < extensionFragment.getSearchIndex())
            this.extension = extensionFragment.getFragment();
    }

    /**
     * 是合法
     * @return 是否
     */
    public boolean isLegal(){
        return folder != null &&
                date != null &&
                UUID != null &&
                fileName != null &&
                extension != null;
    }

    /**
     * 格式字符数组，出路径格式
     *
     *          根目录    文件夹      日期    标识  文件名
     *            |         |         |       |      |
     * WINDOWS: root_path\\folder\\yyyyMMdd\\uuid_fileName
     * LINUX:   root_path/folder/yyyyMMdd/uuid_fileName
     *                                        |
     *                                     分隔符
     * 示例:
     *
     *     -->
     *             indexOf
     *         folder\\                                           文件夹 分隔符(searchIndex)
     *                     +8                                           |
     *                  yyyymmdd\\                                 日期 分隔符(searchIndex)
     *                                +32                              |
     *                               uuid_                         UUID 分隔符(searchIndex)
     *                                                                 |
     *                                      fileName.            文件名 前后缀分隔符(searchIndex)
     *                                                                |
     *                                                jpg    <--   扩展名
     *
     *
     * @param pathArray 路径字符数组
     */
    public void formatCharArray(char[] pathArray) {
        PathFragment folderFragment = PathFragment.valueOfCharArray(
                pathArray, File.separatorChar);
        PathFragment dateFragment = PathFragment.valueOfCharArray(
                pathArray, folderFragment.getSearchIndex() + 1, File.separatorChar);
        PathFragment uuidFragment = PathFragment.valueOfCharArray(
                pathArray, dateFragment.getSearchIndex() + 1, IDGenerator.FID_SEPARATOR);
        PathFragment fileNameFragment = PathFragment.valueOfCharArray(
                pathArray, uuidFragment.getSearchIndex() + 1, '.');
        PathFragment extensionFragment = PathFragment.lastValueOfCharArray(
                pathArray, uuidFragment.getSearchIndex() + 1, '.');

        setFolder(folderFragment);
        setDate(folderFragment, dateFragment);
        setUUID(dateFragment, uuidFragment);
        setFileName(uuidFragment, fileNameFragment);
        setExtension(uuidFragment, extensionFragment);
    }

    /**
     * 格式字符串，出路径对象
     * @param path 字符串
     */
    public void formatString(String path) {
        formatCharArray(path.toCharArray());
    }

    @Override
    public String toString() {
        return "PathFormat{" +
                "folder='" + folder + '\'' +
                ", date='" + date + '\'' +
                ", UUID='" + UUID + '\'' +
                ", fileName='" + fileName + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
