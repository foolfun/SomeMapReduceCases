package test3;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class dailyAccessCount{
	
	public static class MyMapper
		extends Mapper<Object,Text,Text,IntWritable>{
			private final static IntWritable one = new IntWritable(1);
			
			public void map(Object key,Text value,Context context)
				throws IOException,InterruptedException{
				String line = value.toString();
				//指定逗号为分隔符，组成数组
				String array[] = line.split(",");
				//提取数组中的访问日期作为Key
				String keyOutput = array[1];
				//组成键值对
				context.write(new Text(keyOutput),one);
			}
	}
	
	public static class MyReducer
		extends Reducer<Text,IntWritable,Text,IntWritable>{
			private IntWritable result = new IntWritable();
			public void reduce(Text key,Iterable<IntWritable> values,Context context)
				throws IOException,InterruptedException {
					//定义累加器，初始值为0
					int sum = 0;
					for (IntWritable val : values) {
						//将相同键的所有值进行累加
						sum += val.get();
					}
					result.set(sum);
					context.write(key,result);//输出访问日期，总访问次数
			}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();	//初始化相关Hadoop配置
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs(); 
        //输入是一个或多个文件，需要创建一个输入参数的子数组，包括数组的最后一个项目，即MR作业的输出目录
        if (otherArgs.length != 2) { 
         System.out.println(otherArgs.length);
            System.err.println("Usage: wordcount <in> <out>"); 
            System.exit(2);
        } 
        //Job对象指定作业执行规范.用它控制整个作业的运行.在集群上运行这个作业时,要把代码打包成一个JAR文件
        //(Hadoop在集群上发布这个文件).不必明确指定JAR文件的名称.在Job对象的setJarByClass()方法中传递一个类即可
		Job job = Job.getInstance(conf,"Daily Access Count");//新建Job并设置主类
		job.setJarByClass(dailyAccessCount.class);
		
		job.setMapperClass(MyMapper.class);	//为作业设置map类，必须
		job.setReducerClass(MyReducer.class); //为作业设置reduce类，必须	
		
		job.setMapOutputKeyClass(Text.class); //设置map的输出键的格式类型
		job.setMapOutputValueClass(IntWritable.class); //设置map的输出值的格式类型
		
		job.setOutputKeyClass(Text.class); //设置reduce的输出键的格式类型
		job.setOutputValueClass(IntWritable.class); //设置reduce的输出值的格式类型
		
		for (int i = 0; i < args.length - 1; ++ i) {
			FileInputFormat.addInputPath(job, new Path(args[i]));
		}//设置作业HDFS的输入文件，Hadoop可设置多个输入文件，文件名使用逗号隔开
		
		FileOutputFormat.setOutputPath(job, new Path(args[args.length - 1]));//设置作业HDFS的输出目录
		System.exit(job.waitForCompletion(true) ? 0 : 1);//通知集群运行这个作业，并阻塞到作业完成
	}
}
