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
		
		Configuration configuration = new Configuration();//��ʼ�����Hadoop����
		
		//�½�Job����������
		Job job = new Job(configuration,"log_job");
		job.setJarByClass(JobMain.class);
		
		//ָ���Զ����Reduce������reduce������������ֵ�ĸ�ʽ����
		job.setMapperClass(LogMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		//ָ���Զ����Mapper������map������������ֵ�ĸ�ʽ����
		job.setReducerClass(LogReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		Path in=new Path("hdfs://localhost:9000/user/input/5/log.txt"); 
		Path out=new Path("hdfs://localhost:9000/user/output/log_ana_output"); 
		
		//��������·�������·��
		FileInputFormat.addInputPath(job, in);
//		Path path = new Path(args[1]);
		FileSystem fs = out.getFileSystem(configuration);
		if(fs.exists(out)){
			fs.delete(out, true);
		}
		FileOutputFormat.setOutputPath(job, out);
		System.out.println("���ִ�����"); 
		
		//֪ͨ��Ⱥ���������ҵ������������ҵ���
		System.exit(job.waitForCompletion(true)?0:1);
 
	}
 
}
