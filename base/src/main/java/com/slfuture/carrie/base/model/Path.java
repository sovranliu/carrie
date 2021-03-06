package com.slfuture.carrie.base.model;

import com.slfuture.carrie.base.type.List;

import java.io.File;

/**
 * 路径类
 */
public class Path implements Cloneable {
    /**
     * 上级路径
     */
    public static final String PATH_PARENT = "..";
    /**
     * 当前路径
     */
    public static final String PATH_CURRENT = ".";


    /**
     * 分段列表
     */
    protected List<String> sections = null;


    /**
     * 构造函数
     */
    public Path() {
        onNew();
    }

    /**
     * 构造函数
     *
     * @param path 路径
     */
    public Path(String path) {
        onNew();
        for(String piece : path.split(makeSeparator(File.separator))) {
            if(piece.isEmpty()) {
                continue;
            }
            if(PATH_PARENT.equals(piece)) {
                if(0 == sections.size()) {
                    throw new IllegalArgumentException();
                }
                sections.delete(sections.size() - 1);
            }
            else if(PATH_CURRENT.equals(piece)) {
                continue;
            }
            else {
                sections.add(piece);
            }
        }
    }

    /**
     * 构造函数
     *
     * @param path 路径
     * @param separator 分隔符
     */
    public Path(String path, String separator) {
        onNew();
        for(String piece : path.split(makeSeparator(separator))) {
            if(piece.isEmpty()) {
                continue;
            }
            if(PATH_PARENT.equals(piece)) {
                if(0 == sections.size()) {
                    throw new IllegalArgumentException();
                }
                sections.delete(sections.size() - 1);
            }
            else if(PATH_CURRENT.equals(piece)) {
                continue;
            }
            else {
                sections.add(piece);
            }
        }
    }

    /**
     * 判断是否是否是相对路径
     *
     * @param path 路径
     * @return 是否是相对路径
     */
    public static boolean isRelativePath(String path) {
        if(null == path) {
            return false;
        }
        if(path.startsWith(PATH_PARENT) || path.startsWith(PATH_CURRENT)) {
            return true;
        }
        return false;
    }

    /**
     * 跳转
     *
     * @param path 路径
     * @return 操作执行结果
     */
    public Path roll(String path) {
        return roll(path, File.separator);
    }

    /**
     * 跳转
     *
     * @param path 路径
     * @param separator 分隔符
     * @return 操作执行结果
     */
    public Path roll(String path, String separator) {
        Path result = null;
        try {
            if(isRelativePath(path)) {
                result = (Path) this.clone();
            }
            else {
                result = new Path();
            }
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
        for(String piece : path.split(makeSeparator(separator))) {
            if(piece.isEmpty()) {
                continue;
            }
            if(PATH_PARENT.equals(piece)) {
                if(0 == result.sections.size()) {
                    return null;
                }
                result.sections.delete(result.sections.size() - 1);
            }
            else if(PATH_CURRENT.equals(piece)) {
                continue;
            }
            else {
                result.sections.add(piece);
            }
        }
        return result;
    }

    /**
     * 跳转
     *
     * @param path 路径对象
     * @return 操作执行结果
     */
    public Path roll(Path path) throws CloneNotSupportedException {
        Path result = null;
        try {
            result = (Path) this.clone();
        }
        catch (CloneNotSupportedException e) {
            return null;
        }
        for(String piece : path.sections) {
            if(piece.isEmpty()) {
                continue;
            }
            if(PATH_PARENT.equals(piece)) {
                if(0 == result.sections.size()) {
                    return null;
                }
                result.sections.delete(result.sections.size() - 1);
            }
            else if(PATH_CURRENT.equals(piece)) {
                continue;
            }
            else {
                result.sections.add(piece);
            }
        }
        return result;
    }

    /**
     * 字符串
     *
     * @return 字符串
     */
    public String toString() {
        return File.separator + "" + toString(File.separator);
    }

    /**
     * 字符串
     *
     * @param separator 分隔符
     * @return 字符串
     */
    public String toString(String separator) {
        StringBuilder builder = new StringBuilder();
        boolean sentry = false;
        for(String piece : sections) {
            if(sentry) {
                builder.append(separator);
            }
            else {
                sentry = true;
            }
            builder.append(piece);
        }
        return builder.toString();
    }

    /**
     * 比较
     *
     * @param object 被比较对象
     * @return 比较结果
     */
    @Override
    public boolean equals(Object object) {
        Path path = (Path) object;
        if(path.sections.size() != sections.size()) {
            return false;
        }
        int i = 0;
        for(String piece : sections) {
            if(!piece.equals(path.sections.get(i++))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 拷贝
     *
     * @return 拷贝后的对象
     */
    public Object clone() throws CloneNotSupportedException {
        Path path = new Path();
        path.sections = (List<String>) this.sections.clone();
        return path;
    }


    /**
     * 初始化
     */
    public void onNew() {
        sections = new List<String>();
    }

    /**
     * 处理错误正则
     *
     * @param separator 分隔符
     * @return 正确的分隔符
     */
    private String makeSeparator(String separator) {
        return separator.replace("\\", "\\\\");
    }
}
