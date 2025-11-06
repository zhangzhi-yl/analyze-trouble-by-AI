package org.yy.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JDK1.8 文件删除工具类（支持单文件和文件夹）
 */
public class FileDeleteUtil {

    /**
     * 删除单个文件
     * @param filePath 文件路径（如：D:/test.txt 或 /home/user/file.log）
     * @return true：删除成功；false：文件不存在
     * @throws IOException 权限不足、文件被占用等导致删除失败时抛出
     */
    public static boolean deleteFile(String filePath) throws IOException {
        // 校验路径合法性
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件路径不能为空");
        }

        Path path = Paths.get(filePath);

        // 检查文件是否存在
        if (!Files.exists(path)) {
            System.out.println("文件不存在，无需删除：" + filePath);
            return false;
        }

        // 检查是否为文件（非文件夹）
        if (Files.isDirectory(path)) {
            throw new IOException("路径指向的是文件夹，不是文件：" + filePath);
        }

        // 执行删除（JDK1.8支持）
        Files.delete(path);
        System.out.println("文件删除成功：" + filePath);
        return true;
    }

    /**
     * 删除文件夹（支持非空文件夹，递归删除所有内容）
     * @param folderPath 文件夹路径（如：D:/temp 或 /home/user/docs）
     * @return true：删除成功；false：文件夹不存在
     * @throws IOException 权限不足、子文件被占用等导致删除失败时抛出
     */
    public static boolean deleteFolder(String folderPath) throws IOException {
        // 校验路径合法性
        if (folderPath == null || folderPath.trim().isEmpty()) {
            throw new IllegalArgumentException("文件夹路径不能为空");
        }

        Path folder = Paths.get(folderPath);

        // 检查文件夹是否存在
        if (!Files.exists(folder)) {
            System.out.println("文件夹不存在，无需删除：" + folderPath);
            return false;
        }

        // 检查是否为文件夹（非文件）
        if (!Files.isDirectory(folder)) {
            throw new IOException("路径指向的是文件，不是文件夹：" + folderPath);
        }

        // 递归删除文件夹内所有内容（JDK1.8的Stream处理）
        try (Stream<Path> subPaths = Files.list(folder)) {
            // 收集子路径（JDK1.8需用collect，toList()在JDK16+才支持）
            List<Path> children = subPaths.collect(Collectors.toCollection(ArrayList::new));
            for (Path child : children) {
                // 递归删除子文件/子文件夹
                if (Files.isDirectory(child)) {
                    deleteFolder(child.toString());
                } else {
                    Files.delete(child);
                    System.out.println("已删除子文件：" + child);
                }
            }
        }

        // 删除空文件夹本身
        Files.delete(folder);
        System.out.println("文件夹删除成功：" + folderPath);
        return true;
    }

    // 测试示例
    public static void main(String[] args) {
        // 测试删除单个文件
        String filePath = "D:/test.txt";
        try {
            deleteFile(filePath);
        } catch (IOException e) {
            System.err.println("删除文件失败：" + e.getMessage());
        }

        // 测试删除文件夹
        String folderPath = "D:/temp";
        try {
            deleteFolder(folderPath);
        } catch (IOException e) {
            System.err.println("删除文件夹失败：" + e.getMessage());
        }
    }
}
