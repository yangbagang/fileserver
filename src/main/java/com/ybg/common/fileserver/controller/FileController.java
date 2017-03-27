package com.ybg.common.fileserver.controller;

import com.ybg.common.fileserver.util.Coder;
import com.ybg.common.fileserver.util.file.FileUtils;
import com.ybg.common.fileserver.util.file.IDGenerator;
import com.ybg.common.fileserver.util.file.path.IllegalPathFormatException;
import com.ybg.common.fileserver.util.file.path.PathFormat;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(value = "/file")
public class FileController {

    /**
     * 根文件路径
     */
    @Value("${upload.root.path}")
    String ROOT_PATH;

    /**
     * 上传
     *
     * @param folder   文件夹
     * @param partFile 多段文件 上传的文件名称必须为：Filedata
     */
    @RequestMapping(value = "/upload3", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public void upload3(String folder,
                                      @RequestParam("Filedata") MultipartFile partFile,HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (StringUtils.isEmpty(folder)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (FileUtils.ofContainsSymbol(folder)) {
                throw new RuntimeException("文件夹不能包含特许的字符！");
            }
            if (partFile == null || partFile.getSize() <= 0) {
                throw new NullPointerException("文件内容不能为空！");
            }
            String fid = IDGenerator.randomWebpFID(folder);
            if (fid.length() > 255) {
                throw new RuntimeException("文件名不能太长！");
            }
            File uploadFile = new File(ROOT_PATH, fid);
            File pf = uploadFile.getParentFile();
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    throw new RuntimeException("文件夹创建失败！");
                }
            }
            FileUtils.saveToWebp(partFile.getInputStream(), uploadFile);
            map.put("status", HttpServletResponse.SC_OK);
            map.put("fid", Coder.encode(fid));
            System.err.println("=======fid======" + Coder.encode(fid));
            String str = "{\"fid\":" +"\"" + Coder.encode(fid) +"\"" + ",\"status\":" + "\"" + HttpServletResponse.SC_OK +"\""+"}";
            response.getWriter().write(str);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }
    }

    /**
     * 上传
     *
     * @param folder   文件夹
     * @param partFile 多段文件 上传的文件名称必须为：Filedata
     */
    @RequestMapping(value = "/upload2", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    public void upload2(String folder,
                        @RequestParam("Filedata") MultipartFile partFile,HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            if (StringUtils.isEmpty(folder)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (FileUtils.ofContainsSymbol(folder)) {
                throw new RuntimeException("文件夹不能包含特许的字符！");
            }
            if (partFile == null || partFile.getSize() <= 0) {
                throw new NullPointerException("文件内容不能为空！");
            }
            String fid = IDGenerator.randomFIDString(folder,
                    partFile.getOriginalFilename());
            if (fid.length() > 255) {
                throw new RuntimeException("文件名不能太长！");
            }
            File uploadFile = new File(ROOT_PATH, fid);
            File pf = uploadFile.getParentFile();
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    throw new RuntimeException("文件夹创建失败！");
                }
            }
            //保存
            is = partFile.getInputStream();
            bos = new BufferedOutputStream(
                    new FileOutputStream(uploadFile));
            FileUtils.write(bos, is);
            //压缩
            String fid2 = IDGenerator.randomWebpFID(folder);
            File webpFile = new File(ROOT_PATH, fid2);
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    FileUtils.saveToWebp(uploadFile, webpFile);
                }
            }.start();
            map.put("status", HttpServletResponse.SC_OK);
            map.put("fid", Coder.encode(fid2));
            System.err.println("=======fid======" + Coder.encode(fid2));
            String str = "{\"fid\":" +"\"" + Coder.encode(fid2) +"\"" + ",\"status\":" + "\"" + HttpServletResponse.SC_OK +"\""+"}";
            response.getWriter().write(str);
        } catch (Exception e) {
            FileUtils.close(bos, is);
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }
    }

    /**
     * 上传
     *
     * @param folder   文件夹
     * @param partFile 多段文件 上传的文件名称必须为：Filedata
     */
    @RequestMapping(value = "/upload", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> upload(String folder,
                                      @RequestParam("Filedata") MultipartFile partFile) {
    	Map<String, Object> map = new HashMap<>();
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            if (StringUtils.isEmpty(folder)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (FileUtils.ofContainsSymbol(folder)) {
                throw new RuntimeException("文件夹不能包含特许的字符！");
            }
            if (partFile == null || partFile.getSize() <= 0) {
                throw new NullPointerException("文件内容不能为空！");
            }
            String fid = IDGenerator.randomFIDString(folder,
                    partFile.getOriginalFilename());
            if (fid.length() > 255) {
                throw new RuntimeException("文件名不能太长！");
            }
            File uploadFile = new File(ROOT_PATH, fid);
            File pf = uploadFile.getParentFile();
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    throw new RuntimeException("文件夹创建失败！");
                }
            }
            is = partFile.getInputStream();
            bos = new BufferedOutputStream(
                    new FileOutputStream(uploadFile));
            FileUtils.write(bos, is);
            map.put("status", HttpServletResponse.SC_OK);
            map.put("fid", Coder.encode(fid));
        } catch (Exception e) {
        	e.printStackTrace();
            FileUtils.close(bos, is);
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 上传
     *
     * @param folder   文件夹
     * @param fileName 文件名
     * @param fileString 文件内容
     */
    @RequestMapping(value = "/uploadString1", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> uploadString1(String folder, String fileName, String fileString) {
        Map<String, Object> map = new HashMap<>();
        BufferedOutputStream bos = null;
        try {
            if (StringUtils.isEmpty(folder)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (StringUtils.isEmpty(fileName)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (FileUtils.ofContainsSymbol(folder)) {
                throw new RuntimeException("文件夹不能包含特许的字符！");
            }
            if (FileUtils.ofContainsSymbol(fileName)) {
                throw new RuntimeException("文件名不能包含特许的字符！");
            }
            if (fileString == null || fileString.length() <= 0) {
                throw new NullPointerException("文件内容不能为空！");
            }
            String fid = IDGenerator.randomFIDString(folder, fileName);
            if (fid.length() > 255) {
                throw new RuntimeException("文件名不能太长！");
            }
            File uploadFile = new File(ROOT_PATH, fid);
            File pf = uploadFile.getParentFile();
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    throw new RuntimeException("文件夹创建失败！");
                }
            }
            bos = new BufferedOutputStream(
                    new FileOutputStream(uploadFile));
            FileUtils.write(bos, fileString);
            map.put("status", HttpServletResponse.SC_OK);
            map.put("fid", Coder.encode(fid));
        } catch (Exception e) {
            FileUtils.close(bos);
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 二进制上传
     * @param folder
     * @param fileName
     * @param requst
     * @return
     */
    @RequestMapping(value = "/uploadBytes", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> uploadBytes(String folder, String fileName,HttpServletRequest requst) {
        Map<String, Object> map = new HashMap<>();
        BufferedOutputStream bos = null;
        try {
            if (StringUtils.isEmpty(folder)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (StringUtils.isEmpty(fileName)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (FileUtils.ofContainsSymbol(folder)) {
                throw new RuntimeException("文件夹不能包含特许的字符！");
            }
            if (FileUtils.ofContainsSymbol(fileName)) {
                throw new RuntimeException("文件名不能包含特许的字符！");
            }
            String fid = IDGenerator.randomFIDString(folder, fileName);
            if (fid.length() > 255) {
                throw new RuntimeException("文件名不能太长！");
            }
            File uploadFile = new File(ROOT_PATH, fid);
            File pf = uploadFile.getParentFile();
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    throw new RuntimeException("文件夹创建失败！");
                }
            }
            bos = new BufferedOutputStream(
                    new FileOutputStream(uploadFile));
            Object object = requst.getParameter("fileBytes");
            byte[] temp = toByteArray(object);
            FileUtils.write(bos, temp);
            map.put("status", HttpServletResponse.SC_OK);
            map.put("fid", Coder.encode(fid));
        } catch (Exception e) {
            FileUtils.close(bos);
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 上传
     *
     * @param folder   文件夹
     * @param fileName 文件名
     * @param fileString 文件内容
     */
    @SuppressWarnings("static-access")
	@RequestMapping(value = "/uploadString", method = {RequestMethod.OPTIONS, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> uploadString(String folder, String fileName, String fileString,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        OutputStream out = null;
        System.err.println("fileName:" + fileName);
        try {
        	request.setCharacterEncoding("utf-8");
        	System.out.println(new String(fileName.getBytes("ISO-8859-1"),"gb2312")); 
        	System.out.println(new String(fileName.getBytes("UTF8"),"gb2312")); 
        	System.out.println(new String(fileName.getBytes("GB2312"),"gb2312")); 
        	System.out.println(new String(fileName.getBytes("GBK"),"gb2312")); 
        	System.out.println(new String(fileName.getBytes("BIG5"),"gb2312")); 
            if (StringUtils.isEmpty(folder)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (StringUtils.isEmpty(fileName)) {
                throw new RuntimeException("文件夹不能为空！");
            }
            if (FileUtils.ofContainsSymbol(folder)) {
                throw new RuntimeException("文件夹不能包含特许的字符！");
            }
            if (FileUtils.ofContainsSymbol(fileName)) {
                throw new RuntimeException("文件名不能包含特许的字符！");
            }
            if (fileString == null || fileString.length() <= 0) {
                throw new NullPointerException("文件内容不能为空！");
            }
            Base64 base64 = new Base64();
            String folder1 = IDGenerator.randomString(folder);
            File file = new File(ROOT_PATH + File.separator + folder1);
            file.mkdirs();
            String fid = IDGenerator.randomFIDString(folder, fileName);
            String imgFilePath = ROOT_PATH + File.separator + fid;
            byte[] b = base64.decodeBase64(fileString);
            out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            map.put("status", HttpServletResponse.SC_OK);
            map.put("fid", Coder.encode(fid));
        } catch (IOException e) {
            FileUtils.close(out);
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 对象转数组
     * @param obj
     * @return
     */
    public byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 下载
     *
     * @param fid      文件标识
     * @param response HttpServlet响应
     */
    @RequestMapping(value = "/download/{fid}", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> download(@PathVariable  String fid, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        ServletOutputStream sos = null;
        BufferedInputStream bis = null;
        try {
            fid = Coder.decode(fid);
            File uploadFile = new File(ROOT_PATH, fid);
            if (!uploadFile.exists()) {
                throw new FileNotFoundException("对应的文件不存在！");
            }
            PathFormat format = new PathFormat();
            format.formatString(fid);
            if (!format.isLegal()) {
                throw new IllegalPathFormatException("不合法的路径格式！");
            }
            response.setContentLength((int) uploadFile.length());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + format.getEncodedFileName());
            sos = response.getOutputStream();
            bis = new BufferedInputStream(
                    new FileInputStream(uploadFile));
            FileUtils.write(sos, bis);
        } catch (Exception e) {
            FileUtils.close(sos, bis);
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     *
     * @param fids
     * @param response
     * @return
     */
    @RequestMapping(value = "/downloadZip/{fids}", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> downloadZip(@PathVariable  String fids, HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>();
        ZipOutputStream zos = null;
        try {
            String[] fidArray = fids.split(",");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + IDGenerator.sdf.format(new Date()) + ".zip");
            zos = new ZipOutputStream(response.getOutputStream());
            if(fidArray.length == 1){
            	return download(fids,response);
            }
            for (int i = 0; i < fidArray.length; i++) {
                String fid = Coder.decode(fidArray[i]);
                File uploadFile = new File(ROOT_PATH, fid);
                if (!uploadFile.exists()) {
                    throw new FileNotFoundException("对应的文件不存在！");
                }
                PathFormat format = new PathFormat();
                format.formatString(fid);
                if (!format.isLegal()) {
                    throw new IllegalPathFormatException("不合法的路径格式！");
                }
                zos.putNextEntry(new ZipEntry(format.getFullFileName()));
                FileUtils.writeCloseInput(zos,
                        new FileInputStream(uploadFile));
                zos.closeEntry();
            }
        } catch (Exception e) {
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }finally{
        	FileUtils.close(zos);
        }
        return map;
    }

    /**
     * 预览
     *
     * @param fid      文件标识
     * @param response HttpServlet响应
     */
    @RequestMapping(value = "/preview/{fid}", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> preview(@PathVariable String fid, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        ServletOutputStream sos = null;
        BufferedInputStream bis = null;
        try {
            fid = Coder.decode(fid);
            if (!FileUtils.isPreviewExtension(fid)) {
                throw new RuntimeException("预览的文件格式：" +
                        StringUtils.join(FileUtils.PREVIEW_EXTENSIONS, ","));
            }
            File uploadFile = new File(ROOT_PATH, fid);
            if (!uploadFile.exists()) {
                throw new FileNotFoundException("对应的文件不存在！");
            }
            sos = response.getOutputStream();
            bis = new BufferedInputStream(
                    new FileInputStream(uploadFile));
            FileUtils.write(sos, bis);
        } catch (Exception e) {
            FileUtils.close(sos, bis);
            map.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
