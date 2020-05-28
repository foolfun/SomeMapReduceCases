package LogAna;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
 
public class JobMain {
 
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		
		Configuration configuration = new Configuration();//初始化相关Hadoop配置
		
		//新建Job并设置主类
		Job job = new Job(configuration,"log_job");
		job.setJarByClass(JobMain.class);
		
		//指定自定义的Reduce，设置reduce的输出键和输出值的格式类型
		job.setMapperClass(LogMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		//指定自定义的Mapper，设置map的输出键和输出值的格式类型
		job.setReducerClass(LogReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		Path in=new Path("hdfs://localhost:9000/user/input/5/log.txt"); 
		Path out=new Path("hdfs://localhost:9000/user/output/log_ana_output"); 
		
		//配置输入路径与输出路径
		FileInputFormat.addInputPath(job, in);
//		Path path = new Path(args[1]);
		FileSystem fs = out.getFileSystem(configuration);
		if(fs.exists(out)){
			fs.delete(out, true);
		}
		FileOutputFormat.setOutputPath(job, out);
		System.out.println("输出执行完毕"); 
		
		//通知集群运行这个作业，并阻塞到作业完成
		System.exit(job.waitForCompletion(true)?0:1);
 
	}
 
}
