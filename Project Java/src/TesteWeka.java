import weka.classifiers.bayes.NaiveBayes;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class TesteWeka {
	
	public static void main(String[] args) throws Exception {
		
		TesteWeka tw = new TesteWeka();
		/*tw.predict("alexnet_train.arff","alexnet_test.arff","alexnet");
		tw.predict("caffenet_train.arff","caffenet_test.arff","caffenet");
		tw.predict("googlenet_train.arff","googlenet_test.arff","googlenet");
		
		tw.predict("transfer_alexnet_train.arff","transfer_alexnet_test.arff","transfer_alexnet");
		tw.predict("transfer_caffenet_train.arff","transfer_caffenet_test.arff","transfer_caffenet");
		tw.predict("transfer_googlenet_train.arff","transfer_googlenet_test.arff","transfer_googlenet");*/
		
		tw.arquivos("./arquivos/alexnet/test/","arq_alexnet.csv");
		tw.arquivos("./arquivos/caffenet/test/","arq_caffenet.csv");
		tw.arquivos("./arquivos/googlenet/test/","arq_googlenet.csv");
		tw.arquivos("./arquivos/transfer_alexnet/test/","arq_trans_alexnet.csv");
		tw.arquivos("./arquivos/transfer_caffenet/test/","arq_trans_caffenet.csv");
		tw.arquivos("./arquivos/transfer_googlenet/test/","arq_trans_googlenet.csv");
		System.out.println("Fim");
	}
	public void predict(String arqArffTrain, String arqArffTest, String model){
		try{
		
			DataSource dsTrain = new DataSource(arqArffTrain);
		
			Instances ins = dsTrain.getDataSet();
			ins.setClassIndex(5);
	
			
			NaiveBayes nb = new NaiveBayes();
			
			nb.buildClassifier(ins);
			
			DataSource dsTest = new DataSource(arqArffTest);
			Instances insPred = dsTest.getDataSet();
			
			FileWriter arq = new FileWriter("pred_"+model+".csv",true);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.println("noporno;porno");
			
			for(int i = 0 ; i < insPred.numInstances(); i++){
				
				Instance instance = insPred.instance(i);
				
				Instance novoPredict = new Instance(6);
				novoPredict.setDataset(ins);
				
				double prob1 = Double.parseDouble(instance.toString(0));
				double prob2 = Double.parseDouble(instance.toString(1));
				double prob3 = Double.parseDouble(instance.toString(2));
				double prob4 = Double.parseDouble(instance.toString(3));
				double prob5 = Double.parseDouble(instance.toString(4));
				
				novoPredict.setValue(0,prob1);
				novoPredict.setValue(1,prob2);
				novoPredict.setValue(2,prob3);
				novoPredict.setValue(3,prob4);
				novoPredict.setValue(4,prob5);
				
				double prob[] = nb.distributionForInstance(novoPredict);
				
				//System.out.println("noporno: "+prob[0]);
				//System.out.println("porno: "+prob[1]);
				gravarArq.println(prob[0]+";"+prob[1]);

			}
			arq.close();
			System.out.println("Fim predicao!!!");
			
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.print("exe");
		}
	}
	
	public void arquivos(String path, String nameArq){
		try{
			FileWriter arq = new FileWriter(nameArq,true);
			PrintWriter gravarArq = new PrintWriter(arq);

			File file = new File(path);
			File afile[] = file.listFiles();
			int i = 0;
			for (int j = afile.length; i < j; i++) {
				File arquivos = afile[i];
				//System.out.println(arquivos.getName());
				gravarArq.println(arquivos.getName());
			}
			
				
				arq.close();
			
		}
		catch (Exception e) {
				// TODO: handle exception
		}
	}
}
