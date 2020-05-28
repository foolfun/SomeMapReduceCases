package LogAna;


import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
 
public class LogReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
 
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,Context context)
			throws IOException, InterruptedException {
		int sum = 0;//定义累加器，初始值为0
		for(IntWritable val : values){
			sum += val.get();//将相同键的所有值进行累加
		}
		context.write(key, new IntWritable(sum));//输出日志数据出现次数
		
	}
 
}