package test3;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;   
import org.apache.hadoop.io.NullWritable;  
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.Reducer;  
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;  
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
//import Utils.FindHDFSText;

public class DataDeduplication{  

    public static class Map extends Mapper<Object , Text , Text , NullWritable>{
        
    	private static Text newKey=new Text();  
    	public void map(Object key,Text value,Context context) 
    			throws IOException, InterruptedException{  
		    String line=value.toString();  
		    System.out.println("line是："+line);
		    if(line!=null){
			    String arr[]=line.split(" ");//组成数组
			    System.out.println("a[1]是"+arr[1]);
			    newKey.set(arr[1]);  //提取数组中的商品id作为Key
			    context.write(newKey, NullWritable.get());  //组成键值对
			    System.out.println("新的截取的值是"+newKey); } 
		    }  
		}  
    
    public static class Reduce extends Reducer<Text, NullWritable, Text, NullWritable>{  
    	public void reduce(Text key,Iterable<NullWritable> values,Context context) 
    			throws IOException, InterruptedException{  
    		context.write(key,NullWritable.get());
        	}  
        }
    
    public static void main(String[] args) 
    		throws IOException, ClassNotFoundException, InterruptedException{  
        Configuration conf=new Configuration(); //初始化相关Hadoop配置
//        FindHDFSText find = new FindHDFSText();
        conf.set("dfs.client.use.datanode.hostname", "true");
        System.out.println("start");  
        
        @SuppressWarnings("deprecation")
        Job job =new Job(conf,"filter");  //新建Job并设置主类
        job.setJarByClass(DataDeduplication.class);
        System.out.println("fiter执行完毕");
        job.setMapperClass(Map.class); //指定要用的map类型
        System.out.println("map执行完毕");
        job.setReducerClass(Reduce.class);//指定要用的reduce类型
        System.out.println("reduce执行完毕");
        
        job.setOutputKeyClass(Text.class);  //控制reduce函数的输出键的格式类型
        job.setOutputValueClass(NullWritable.class);  //控制reduce函数的输出值的格式类型
        
        job.setInputFormatClass(TextInputFormat.class); 
        System.out.println("TextInputFormat执行完毕"); 
        job.setOutputFormatClass(TextOutputFormat.class);  
        Path in=new Path("hdfs://localhost:9000/user/input/4/dataset.txt"); 
        System.out.println("in执行完毕");
        Path out=new Path("hdfs://localhost:9000/user/output/resultfour");
        System.out.println("out执行完毕");
        Path path = new Path("hdfs://localhost:9000/user/output/resultfour");
        
        FileSystem fileSystem = path.getFileSystem(conf);// 根据path找到这个文件
        if (fileSystem.exists(path)) {
            fileSystem.delete(path, true);// true的意思是，就算output有东西，也一带删除
        }
        FileInputFormat.addInputPath(job,in);  
        System.out.println("读入执行完毕");
        FileOutputFormat.setOutputPath(job,out);
        System.out.println("输出执行完毕"); 
       
        System.exit(job.waitForCompletion(true) ? 0 : 1);  
    }  
}
