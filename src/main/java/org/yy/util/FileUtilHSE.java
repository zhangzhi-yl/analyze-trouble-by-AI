package org.yy.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;

/**
 * 说明：文件处理
 * 作者：YuanYes Q 313-596-790
 * 官网：356703572@qq.com
 */
public class FileUtilHSE {


	/**获取文件大小 返回 KB 保留3位小数  没有文件时返回0
	 * @param filepath 文件完整路径，包括文件名
	 * @return
	 */
	public static Double getFilesize(String filepath){
		File backupath = new File(filepath);
		return Double.valueOf(backupath.length())/1000.000;
	}

	/**
	 * 创建目录
	 * @param destDirName目标目录名
	 * @return
	 */
	public static Boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if(!dir.getParentFile().exists()){				//判断有没有父路径，就是判断文件整个路径是否存在
			return dir.getParentFile().mkdirs();		//不存在就全部创建
		}
		return false;
	}

	/**
	 * 删除文件
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myDelFile = new File(filePath);
			myDelFile.delete();
		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 读取到字节数组0
	 * @param filePath //路径
	 * @throws IOException
	 */
	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		fi.close();
		return buffer;
	}

	/**
	 * 读取到字节数组1
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * 读取到字节数组2
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray2(String filePath) throws IOException {
		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	 *
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray3(String filePath) throws IOException {

		FileChannel fc = null;
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "r");
			fc = rf.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
					fc.size()).load();
			//System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				// System.out.println("remain");
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				rf.close();
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 往文件里的内容
	 * @param content  写入的内容
	 */
	public static void writeFile(String fileP,String content){
		String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))+"../../";	//项目路径
		filePath = filePath.replaceAll("file:/", "");
		filePath = filePath.replaceAll("%20", " ");
		filePath = filePath.trim() + fileP.trim();
		if(filePath.indexOf(":") != 1){
			filePath = File.separator + filePath;
		}
		try {
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath),"utf-8");
	        BufferedWriter writer=new BufferedWriter(write);
	        writer.write(content);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 往文件里的内容（Projectpath下）
	 * @param content  写入的内容
	 */
	public static void writeFileCR(String fileP,String content){
		String filePath = PathUtil.getProjectpath() + fileP;
		try {
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath),"utf-8");
	        BufferedWriter writer=new BufferedWriter(write);
	        writer.write(content);
	        writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**读取文本里的全部内容
	 * @param filePath  文件路径
	 * @param encoding  编码
	 * @return
	 */
	public static String readTxtFileAll(String filePath, String encoding) {
		StringBuffer fileContent = new StringBuffer();
		try {
			filePath = filePath.replaceAll("file:/", "");
			filePath = filePath.replaceAll("%20", " ");
			filePath = filePath.trim();
			if(filePath.indexOf(":") != 1){
				filePath = File.separator + filePath;
			}
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { 		// 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);	// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					fileContent.append(lineTxt);
					fileContent.append("\n");
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件,查看此路径是否正确:"+filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return fileContent.toString();
	}

	/**
	 * 读取Projectpath某文件里的全部内容
	 * @param filePath  文件路径
	 */
	public static String readFileAllContent(String fileP) {
		StringBuffer fileContent = new StringBuffer();
		try {
			String encoding = "utf-8";
			File file = new File(PathUtil.getProjectpath() + fileP);//文件路径
			if (file.isFile() && file.exists()) { 		// 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);	// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					fileContent.append(lineTxt);
					fileContent.append("\n");
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件,查看此路径是否正确:"+fileP);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return fileContent.toString();
	}
	/**
	 * 文件复制
	 *
	 * @param oldPath 原路径
	 * @param newPath 新路径
	 * @return
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		try {
			File oldpaths = new File(oldPath);
			File newpaths = new File(newPath);
			if (!newpaths.exists()) {
				newpaths.getParentFile().mkdirs();
				Files.copy(oldpaths.toPath(), newpaths.toPath());
			} else {
				newpaths.delete();
				Files.copy(oldpaths.toPath(), newpaths.toPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static MultipartFile convert(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException("文件不存在: " + filePath);
		}

		return new MultipartFile() {
			@Override
			public String getName() {
				return file.getName();
			}

			@Override
			public String getOriginalFilename() {
				return file.getName();
			}

			@Override
			public String getContentType() {
				try {
					return Files.probeContentType(file.toPath());
				} catch (IOException e) {
					return null;
				}
			}

			@Override
			public boolean isEmpty() {
				return file.length() == 0;
			}

			@Override
			public long getSize() {
				return file.length();
			}

			@Override
			public byte[] getBytes() throws IOException {
				try (InputStream is = new FileInputStream(file)) {
					byte[] bytes = new byte[(int) file.length()];
					int read = is.read(bytes);
					if (read != file.length()) {
						throw new IOException("读取文件不完整");
					}
					return bytes;
				}
			}

			@Override
			public InputStream getInputStream() throws IOException {
				return new FileInputStream(file);
			}

			@Override
			public void transferTo(File dest) throws IOException, IllegalStateException {
				Files.copy(file.toPath(), dest.toPath());
			}
		};
	}
}
