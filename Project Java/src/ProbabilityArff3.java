import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instance;
import weka.core.Instances;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ProbabilityArff3 {
	
	public static void main(String[] args) throws Exception {
		
		ProbabilityArff3 tw = new ProbabilityArff3();
		
		tw.predict("./ensemble_arff2/","ind_graf_alexnet_union.arff","grafico_alexnet");
		tw.predict("./ensemble_arff2/","ind_graf_caffenet_union.arff","grafico_caffenet");
		tw.predict("./ensemble_arff2/","ind_graf_googlenet_union.arff","grafico_googlenet");
		tw.predict("./ensemble_arff2/","ind_graf_transfer_alexnet_union.arff","grafico_transfer_alexnet");
		tw.predict("./ensemble_arff2/","ind_graf_transfer_caffenet_union.arff","grafico_transfer_caffenet");
		tw.predict("./ensemble_arff2/","ind_graf_transfer_googlenet_union.arff","grafico_transfer_googlenet");
		
		
	}

	public void predict(String path, String arqArffTrain, String ArqOut){
		try{
		
			DataSource dsTrain = new DataSource(path+arqArffTrain);
		
			Instances ins = dsTrain.getDataSet();
			ins.setClassIndex(2);
	
			//RandomForest rf = new RandomForest();
			//rf.setNumTrees(100);
			//rf.buildClassifier(ins);
			
			//DataSource dsTest = new DataSource(path+arqArffTest);
			//Instances insPred = dsTest.getDataSet();
			//insPred.setClassIndex(2);
			
			List<Predicao> listaNoPorno = new ArrayList<Predicao>();
			List<Predicao> listaPorno = new ArrayList<Predicao>();
			
			for(int i = 0 ; i < ins.numInstances(); i++){
				
				Instance instance = ins.instance(i);
				Predicao pred = new Predicao();
				pred.setIdFrame(i);
				pred.setInstancia(instance);
				
				String strProb = instance.toString(0);
				double prob = (double) Double.parseDouble(instance.toString(1));
				
				if(strProb.equals("0")){
					pred.setClasse(0.0d);
					pred.setProbability(prob);
					listaNoPorno.add(pred);
				}
				else{
					
					pred.setClasse(1.0d);
					pred.setProbability(prob);
					listaPorno.add(pred);
					
				}
			}
					
			/*for(int i = 0 ; i < insPred.numInstances(); i++){
				
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
			*/
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
					
					FileWriter arq = new FileWriter(ArqOut+".csv",true);
					PrintWriter gravarArq = new PrintWriter(arq);
					gravarArq.println(rejrate + ";" + acc);
					arq.close();
					
				}	
			}
			System.out.println("Fim "+ArqOut);
		}
		
		catch (Exception e) {
			// TODO: handle exception
			System.out.print("Exception"+e);
		}
	}
}
