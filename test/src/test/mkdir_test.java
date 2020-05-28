package test;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;

public class mkdir_test {
	org.apache.hadoop.conf.Configuration conf = null;
	FileSystem fs = null;
	@Before
	public void conn() throws IOException{
		conf = new org.apache.hadoop.conf.Configuration();
		fs = FileSystem.get(conf);
	}
	
//	@Test
//	public void mkdir() throws IOException{
//		Path path = new Path("/mytemp");
//		if(fs.exists(path))
//			fs.delete(path,true);
//		fs.mkdirs(path);
//	}
	
	
//	@Test
//	public void uploadFile() throws IOException{
////		创建文件上传路径
//		Path path = new Path("/mytemp/haha.txt");
//		FSDataOutputStream fods = fs.create(path);
////		拿到磁盘的文件
//		InputStream is = new BufferedInputStream(new FileInputStream("D:/keke.txt"));
//		
//		IOUtils.copyBytes(is, fods, conf, true);
//	}
	
	
	@Test
	public void readFile() throws IOException{
		
		Path f = new Path("/mytemp/haha.txt");
//		FileStatus file = fs.getFileStatus(f);
//		BlockLocation[] blks = fs.getFileBlockLocations(f, 0, file.getLen());
//		//遍历数组
//		for (BlockLocation blk : blks){
//			System.out.print(blk);
//		}
		
		//读文件
		FSDataInputStream fdis = fs.open(f);
		fdis.seek(0);//可以设定读取的开始的位置
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		System.out.print((char)fdis.readByte());
		
	}
	
	@After
	public void close() throws IOException{
		fs.close();
	}
	
}
