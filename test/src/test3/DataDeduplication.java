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
		    System.out.println("line�ǣ�"+line);
		    if(line!=null){
			    String arr[]=line.split(" ");//�������
			    System.out.println("a[1]��"+arr[1]);
			    newKey.set(arr[1]);  //��ȡ�����е���Ʒid��ΪKey
			    context.write(newKey, NullWritable.get());  //��ɼ�ֵ��
			    System.out.println("�µĽ�ȡ��ֵ��"+newKey); } 
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
        Configuration conf=new Configuration(); //��ʼ�����Hadoop����
//        FindHDFSText find = new FindHDFSText();
        conf.set("dfs.client.use.datanode.hostname", "true");
        System.out.println("start");  
        
        @SuppressWarnings("deprecation")
        Job job =new Job(conf,"filter");  //�½�Job����������
        job.setJarByClass(DataDeduplication.class);
        System.out.println("fiterִ�����");
        job.setMapperClass(Map.class); //ָ��Ҫ�õ�map����
        System.out.println("mapִ�����");
        job.setReducerClass(Reduce.class);//ָ��Ҫ�õ�reduce����
        System.out.println("reduceִ�����");
        
        job.setOutputKeyClass(Text.class);  //����reduce������������ĸ�ʽ����
        job.setOutputValueClass(NullWritable.class);  //����reduce���������ֵ�ĸ�ʽ����
        
        job.setInputFormatClass(TextInputFormat.class); 
        System.out.println("TextInputFormatִ�����"); 
        job.setOutputFormatClass(TextOutputFormat.class);  
        Path in=new Path("hdfs://localhost:9000/user/input/4/dataset.txt"); 
        System.out.println("inִ�����");
        Path out=new Path("hdfs://localhost:9000/user/output/resultfour");
        System.out.println("outִ�����");
        Path path = new Path("hdfs://localhost:9000/user/output/resultfour");
        
        FileSystem fileSystem = path.getFileSystem(conf);// ����path�ҵ�����ļ�
        if (fileSystem.exists(path)) {
            fileSystem.delete(path, true);// true����˼�ǣ�����output�ж�����Ҳһ��ɾ��
        }
        FileInputFormat.addInputPath(job,in);  
        System.out.println("����ִ�����");
        FileOutputFormat.setOutputPath(job,out);
        System.out.println("���ִ�����"); 
       
        System.exit(job.waitForCompletion(true) ? 0 : 1);  
    }  
}
