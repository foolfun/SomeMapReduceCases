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
				//ָ������Ϊ�ָ������������
				String array[] = line.split(",");
				//��ȡ�����еķ���������ΪKey
				String keyOutput = array[1];
				//��ɼ�ֵ��
				context.write(new Text(keyOutput),one);
			}
	}
	
	public static class MyReducer
		extends Reducer<Text,IntWritable,Text,IntWritable>{
			private IntWritable result = new IntWritable();
			public void reduce(Text key,Iterable<IntWritable> values,Context context)
				throws IOException,InterruptedException {
					//�����ۼ�������ʼֵΪ0
					int sum = 0;
					for (IntWritable val : values) {
						//����ͬ��������ֵ�����ۼ�
						sum += val.get();
					}
					result.set(sum);
					context.write(key,result);//����������ڣ��ܷ��ʴ���
			}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();	//��ʼ�����Hadoop����
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs(); 
        //������һ�������ļ�����Ҫ����һ����������������飬������������һ����Ŀ����MR��ҵ�����Ŀ¼
        if (otherArgs.length != 2) { 
         System.out.println(otherArgs.length);
            System.err.println("Usage: wordcount <in> <out>"); 
            System.exit(2);
        } 
        //Job����ָ����ҵִ�й淶.��������������ҵ������.�ڼ�Ⱥ�����������ҵʱ,Ҫ�Ѵ�������һ��JAR�ļ�
        //(Hadoop�ڼ�Ⱥ�Ϸ�������ļ�).������ȷָ��JAR�ļ�������.��Job�����setJarByClass()�����д���һ���༴��
		Job job = Job.getInstance(conf,"Daily Access Count");//�½�Job����������
		job.setJarByClass(dailyAccessCount.class);
		
		job.setMapperClass(MyMapper.class);	//Ϊ��ҵ����map�࣬����
		job.setReducerClass(MyReducer.class); //Ϊ��ҵ����reduce�࣬����	
		
		job.setMapOutputKeyClass(Text.class); //����map��������ĸ�ʽ����
		job.setMapOutputValueClass(IntWritable.class); //����map�����ֵ�ĸ�ʽ����
		
		job.setOutputKeyClass(Text.class); //����reduce��������ĸ�ʽ����
		job.setOutputValueClass(IntWritable.class); //����reduce�����ֵ�ĸ�ʽ����
		
		for (int i = 0; i < args.length - 1; ++ i) {
			FileInputFormat.addInputPath(job, new Path(args[i]));
		}//������ҵHDFS�������ļ���Hadoop�����ö�������ļ����ļ���ʹ�ö��Ÿ���
		
		FileOutputFormat.setOutputPath(job, new Path(args[args.length - 1]));//������ҵHDFS�����Ŀ¼
		System.exit(job.waitForCompletion(true) ? 0 : 1);//֪ͨ��Ⱥ���������ҵ������������ҵ���
	}
}
