package test3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
 
import java.io.IOException;
import java.util.StringTokenizer;
 
public class WordCount {
	/**
	 * 
	 *map 函数的输入的key、输入的value、输出的key、输出的value
	 *Text==String
	 *IntWritable==Integer
	 *
	 */
    public static class TokenizerMapper extends Mapper<Object,Text,Text,IntWritable>{
        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {//content用于输出内容的写入
        	/**
        	 * map（）方法的输入是一个键和一个值。我们首先将包含一行输入的Text值转换成java中的string类型
        	 */
   
        	//这是一个分割字符串的类,java中默认的分隔符是:"空格","\t"制表符,"\n"换行符,"\r"回车符
            StringTokenizer itr = new StringTokenizer(value.toString());
            
            while(itr.hasMoreTokens()){//判断是否还有分隔符
                word.set(itr.nextToken());//下一个字符串转换为Text类型
              //String nextToken（）：返回从当前位置到下一个分隔符的字符串。
                context.write(word,one);
            }
        }
    }
 
    public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
    	/**
    	 * reduce函数也有四个形式参数类型用于指定输入和输出类型.
    	 * reduce函数的输入类型必须匹配map函数的输出类型:即Text类型和Intwritable；
    	 * 在这种情况下，reduce的输出也是Text和Intwritable
    	 */
    	
        private IntWritable result = new IntWritable();
 
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        	
            int sum = 0;
            for(IntWritable val : values){
                sum+=val.get();
            }
            result.set(sum);
            context.write(key,result);
        }
    }
 
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        
       //Job对象指定作业执行规范.用它控制整个作业的运行.在集群上运行这个作业时,要把代码打包成一个JAR文件
        Job job = Job.getInstance(conf,"word count");
        
       //(Hadoop在集群上发布这个文件).不必明确指定JAR文件的名称.在Job对象的setJarByClass()方法中传递一个类即可
        job.setJarByClass(WordCount.class);
        
      //指定要用的map类型
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
      //指定要用的reduce类型
        job.setReducerClass(IntSumReducer.class);
      //控制reduce函数的输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
      //定义输入数据的路径,可以是单个文件,也可以是一个目录(此时,将目录下所有文件当做输入)
        FileInputFormat.addInputPath(job,new Path(args[0]));
        
      //定义输出路径,指定reduce函数输出文件的写入目录.在运行作业前该目录是不应该存在的,否则Hadoop会报错并拒绝运行作业.
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        
        //waitForCompletion()方法提交作业并等待执行完成.该方法唯一的参数是一个标识,指示是否已生成详细输出.当标识为true(成功)时,作业会把其进度信息写到控制台
        System.exit(job.waitForCompletion(true) ? 0 : 1);
      
    }
}