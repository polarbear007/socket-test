package lesson11_网络编程05_TCP协议01_上传多级文件夹之封装文件信息对象版;

import java.io.Serializable;

public class FileMessage implements Serializable {
	private static final long serialVersionUID = 6246804069188740138L;
	private String type;
	private String name;
	private long length;
	private String parentPath;

	public FileMessage() {
		super();
	}

	public FileMessage(String type, String name, long length, String path) {
		super();
		this.type = type;
		this.name = name;
		this.length = length;
		this.parentPath = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String path) {
		this.parentPath = path;
	}

	@Override
	public String toString() {
		return "FileMessage [type=" + type + ", name=" + name + ", length=" + length + ", parentPath=" + parentPath
				+ "]";
	}
}
