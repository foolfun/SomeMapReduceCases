package LogAna;

import java.io.IOException;
import javax.naming.spi.DirStateFactory.Result;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
 
public class LogMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
 
	private IntWritable val = new IntWritable(1);
	@Override
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		
		String line = value.toString().trim();
		String tmp = handlerLog(line);//调用清洗程序实现数据清洗
		if(tmp.length()>0){
			context.write(new Text(tmp), val);//提取清洗后的数据作为Key，组成键值对
		}
	}
 
	//127.0.0.1 - - [03/Jul/2014:23:36:38 +0800] "GET /course/detail/3.htm HTTP/1.0" 200 38435 0.038
	private String handlerLog(String line){
		String result = "";	//存储清洗后的数据
		try{
			if(line.length()>20){
				if(line.indexOf("GET")>0){	//如果是GET日志，获取GET-HTTP/1.0的数据段
					result = line.substring(line.indexOf("GET"), line.indexOf("HTTP/1.0")).trim();
//					System.out.println(line.substring(line.indexOf("GET"), line.indexOf("HTTP/1.0")));
//					System.out.println(line.indexOf("GET"));
//					System.out.println(result);
				}else if(line.indexOf("POST")>0){	//如果是POST日志，获取POST-HTTP/1.0的数据段
					result = line.substring(line.indexOf("POST"), line.indexOf("HTTP/1.0")).trim();
				}
			}
		}catch (Exception e) {
			System.out.println(line);
		}
		
		return result;	//返回清洗后的数据
	}
	
	public static void main(String[] args){
		String line = "127.0.0.1 - - [03/Jul/2014:23:36:38 +0800] \"GET /course/detail/3.htm HTTP/1.0\" 200 38435 0.038";
		System.out.println(new LogMapper().handlerLog(line));	//先调用清洗程序实现数据清洗，在对清洗后的数据进行map
	}
}

