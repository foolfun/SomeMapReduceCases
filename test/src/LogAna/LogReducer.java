package LogAna;


import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
 
public class LogReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
 
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,Context context)
			throws IOException, InterruptedException {
		int sum = 0;//�����ۼ�������ʼֵΪ0
		for(IntWritable val : values){
			sum += val.get();//����ͬ��������ֵ�����ۼ�
		}
		context.write(key, new IntWritable(sum));//�����־���ݳ��ִ���
		
	}
 
}