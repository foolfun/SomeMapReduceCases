package stu.Q1;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class MyScoreMapReduce {
    public static class MyMapper extends Mapper<LongWritable,Text,StudentScore,Text> {
       Text ovalue=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//读取一行数据进行切分
            String[] fields = value.toString().split("\t");
            int chinese=Integer.parseInt(fields[3]);   
            int math=Integer.parseInt(fields[4]);
            int english=Integer.parseInt(fields[5]);
            int sum=chinese+math+english;
            Double avg=(1.0)*sum/3;
            StudentScore ss=new StudentScore(fields[2],sum,avg); //创建自定义类对象
           ovalue.set(fields[0]+"\t"+fields[1]);
           context.write(ss,ovalue);
        }
    }
    public static class MyReduce extends Reducer<StudentScore,Text,Text,StudentScore>{

        @Override
        protected void reduce(StudentScore key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for(Text v: values){
                context.write(v,key);
            }
        }
    }
   public static void main(String[] args) {
      Configuration conf=new Configuration();
      System.setProperty("HADOOP_HOME_USER","zsl");
      conf .set( "fs.defaultFS" , "hdfs://localhost:9000" );
      try {
          Job job=Job.getInstance(conf);
          job.setJarByClass(MyScoreMapReduce.class);
          job.setMapperClass(MyMapper.class);
          job.setReducerClass(MyReduce.class);
           job.setMapOutputKeyClass(StudentScore.class);
          job.setMapOutputValueClass(Text.class);
           job.setOutputKeyClass(Text.class);
          job.setOutputValueClass(StudentScore.class);
           Path inpath=new Path("/student/score.txt");
          FileInputFormat.addInputPath(job,inpath);
          Path outpath=new Path("/student/result01");
          if(outpath.getFileSystem(conf).exists(outpath)){
              outpath.getFileSystem(conf).delete(outpath,true);
          }
          FileOutputFormat.setOutputPath(job,outpath);

          job.waitForCompletion(true);
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}