import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.RandomForest;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instance;
import weka.core.Instances;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ProbabilityArff2 {
	
	public static void main(String[] args) throws Exception {
		
		ProbabilityArff2 tw = new ProbabilityArff2();
		
		tw.predict("./unique/10frames/","unique_alexnet_grafico_train.arff","unique_alexnet_grafico_union.arff","grafico_alexnet_10f");
		tw.predict("./unique/10frames/","unique_caffenet_grafico_train.arff","unique_caffenet_grafico_union.arff","grafico_caffenet_10f");
		tw.predict("./unique/10frames/","unique_googlenet_grafico_train.arff","unique_googlenet_grafico_union.arff","grafico_googlenet_10f");
		tw.predict("./unique/10frames/","unique_transfer_alexnet_grafico_train.arff","unique_transfer_alexnet_grafico_union.arff","grafico_transfer_alexnet_10f");
		tw.predict("./unique/10frames/","unique_transfer_caffenet_grafico_train.arff","unique_transfer_caffenet_grafico_union.arff","grafico_transfer_caffenet_10f");
		tw.predict("./unique/10frames/","unique_transfer_googlenet_grafico_train.arff","unique_transfer_googlenet_grafico_union.arff","grafico_transfer_googlenet_10f");
		
	}

	public void predict(String path, String arqArffTrain, String arqArffTest, String modelArq){
		try{
		
			DataSource dsTrain = new DataSource(path+arqArffTrain);
		
			Instances ins = dsTrain.getDataSet();
			ins.setClassIndex(6);
	
			NaiveBayes rf = new NaiveBayes();
			//rf.setUseSupervisedDiscretization(true);
			rf.setUseKernelEstimator(true);
			rf.buildClassifier(ins);
			
			DataSource dsTest = new DataSource(path+arqArffTest);
			Instances insPred = dsTest.getDataSet();
			insPred.setClassIndex(6);
			
			List<Predicao> listaNoPorno = new ArrayList<Predicao>();
			List<Predicao> listaPorno = new ArrayList<Predicao>();
			
			for(int i = 0 ; i < insPred.numInstances(); i++){
				
				Instance instance = insPred.instance(i);
				
				double prob[] = rf.distributionForInstance(instance);
				
				Predicao pred = new Predicao();
				pred.setIdFrame(i);
				pred.setInstancia(instance);
				
				if(prob[0] > prob[1]){
					
					pred.setClasse(0.0d);
					pred.setProbability(prob[0]);
					listaNoPorno.add(pred);
				}
				else{
					pred.setClasse(1.0d);
					pred.setProbability(prob[1]);
					listaPorno.add(pred);
				}
			}
			
			Collections.sort(listaNoPorno);
			Collections.sort(listaPorno);
			
			for(int i = 0; i < 100; i++){
				for(int j = 0; j < 100; j++){
	
					int qtVerificarNormal = (int) ((listaNoPorno.size() * i)/100.0f);
					int qtVerificarPorno = (int) ((listaPorno.size() * i)/100.0f);
					
					int acerto = 0;
					
					for(int k = 0; k < qtVerificarNormal; k++){
						Predicao pred = listaNoPorno.get(k);
						if(pred.getClasse() == pred.getInstancia().classValue()){
							acerto++;
						}
					}
					
					for(int k = 0; k < qtVerificarPorno; k++){
						Predicao pred = listaPorno.get(k);
						if(pred.getClasse() == pred.getInstancia().classValue()){
							acerto++;
						}
					}
					
					float rejrate = 1.0f - ((qtVerificarNormal + qtVerificarPorno) /(float) (listaNoPorno.size() + listaPorno.size()));
					float acc = acerto / (float)(qtVerificarNormal+qtVerificarPorno);
					
					FileWriter arq = new FileWriter(modelArq+".csv",true);
					PrintWriter gravarArq = new PrintWriter(arq);
					gravarArq.println(rejrate + ";" + acc);
					arq.close();
					
				}
				System.out.println("Fim");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.print("Exception"+e);
		}
	}
}
