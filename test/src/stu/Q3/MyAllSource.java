package stu.Q3;

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


public class MyAllSource {
    public static class MyMapper extends Mapper<LongWritable,Text,MyClassAndScore,Text>{
 
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split("\t");
            int chinese=Integer.parseInt(fields[3]);
            int math=Integer.parseInt(fields[4]);
            int english=Integer.parseInt(fields[5]);
            int sum=chinese+math+english;
            MyClassAndScore ms=new MyClassAndScore(fields[0],sum);
            context.write(ms,new Text(fields[1]+"\t"+fields[2]));
        }
    }
  public static class MyReducer extends Reducer<MyClassAndScore,Text,Text,Text>{
 
        @Override
      protected void reduce(MyClassAndScore key, Iterable<Text> values, Context  context) throws IOException, InterruptedException {
            int count=0;
            for(Text v:values){
              count++;
              if(count<=5) {
                  context.write(new Text(key.getClassname()), new Text(v.toString() + "\t" + key.getSum()));
              }
          }
      }
  }
    public static void main(String[] args) {
        Configuration conf =new Configuration();
        System.setProperty("HADOOP_USER_NAME", "zsl");
        conf .set( "fs.defaultFS" , "hdfs://localhost:9000" );
        try {
            Job job=Job.getInstance(conf);
            job.setJarByClass(MyAllSource .class);
            job.setMapperClass(MyMapper.class);
            job.setReducerClass(MyReducer.class);
 
            job.setMapOutputKeyClass(MyClassAndScore.class);
            job.setMapOutputValueClass(Text.class);
 
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
 
            job.setGroupingComparatorClass(MyGroup2.class);
 
            //指定需要统计的文件输入路径
            Path inpath=new Path("/student/score.txt");
            FileInputFormat.addInputPath(job, inpath);
 
            //指定输出目录 输出路径不能存在，否则就会报错 默认是覆盖式的输出
            Path outpath=new Path("/student/result03");
            if(outpath.getFileSystem(conf).exists(outpath)){
                outpath.getFileSystem(conf).delete(outpath,true);
            }
            FileOutputFormat.setOutputPath(job, outpath);
 
            job.waitForCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}