import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

public class AnalisarArff {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AnalisarArff ma = new AnalisarArff();
		
		//ma.loadFile("./arquivos3/juca/","ens_all_juca2_new5",5);
		ma.loadFile("./arquivos3/noporno/","ens_all_noporno_new10",10);
		ma.loadFile("./arquivos3/porno/","ens_all_porno_new10",10);
		ma.loadFile("./arquivos3/talvez/","ens_all_talvez_new10",10);
		ma.loadFile("./arquivos3/train/","ens_all_train_new10",10);
		ma.loadFile("./arquivos3/val/","ens_all_val_new10",10);
		
		System.out.println("All files process!!!");

	}

	public List<String> getList(DataSource dsTrain, int qtdeFrames, int idPred){
		try{
			
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < qtdeFrames; i++){
				list.add("2");
			}
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String strPred = insTrain.toString(idPred);
				list.add(strPred);
			}
			for(int i = 0; i < qtdeFrames; i++){
				list.add("3");
			}
			
			return list;
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public String getStringList(DataSource dsTrain, int qtdeFrames, int idPred, int instancia){
		try{
			
			List<String> list = getList(dsTrain, qtdeFrames,idPred);
			
			String str = "";
			
			for(int i = 0; i < ((qtdeFrames*2)+1); i++){
				str = str + list.get(instancia+i);
				
			}
			
			int frameBase = getBasePorno(str,qtdeFrames);
			double probInterval = getProbabilityIntervalPorno(str, qtdeFrames);
			double probPrevious = getProbabilityPreviousPorno(str, qtdeFrames);
			double probNext = getProbabilityNextPorno(str, qtdeFrames);
			int timePrevious = getTimePreviousPorno(str, qtdeFrames);
			int timeNext = getTimeNextPorno(str, qtdeFrames);
				
			String strAux = ""+frameBase+","+probInterval+","+probPrevious+","+probNext+","+timePrevious+","+timeNext+",";
				
			return strAux;
			
		}
		catch (Exception e) {
		// TODO: handle exception
		}
		return null;	
	}
	public void gerarArffTrain(String arqArff, String model, int qtdeFrames){
		try{
			
			DataSource dsTrain = new DataSource(arqArff);
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
			
				FileWriter arq = new FileWriter(model+".arff",true);
				PrintWriter gravarArq = new PrintWriter(arq);
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(7);
	
				String strAlexnet = getStringList(dsTrain, qtdeFrames,1,i);
				String strCaffenet = getStringList(dsTrain, qtdeFrames,2,i);
				String strGooglenet = getStringList(dsTrain, qtdeFrames,3,i);
				String strTrasferAlexnet = getStringList(dsTrain, qtdeFrames,4,i);
				String strTransferCaffenet = getStringList(dsTrain, qtdeFrames,5,i);
				String strTransferGooglenet = getStringList(dsTrain, qtdeFrames,6,i);
				
				String strArq = strAlexnet + strCaffenet + strGooglenet + strTrasferAlexnet + strTransferCaffenet + strTransferGooglenet + classe;
				
				gravarArq.println(strArq);
				arq.close();
					
				System.out.println("Arquivo "+arqArff+" OK!!!");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void gerarArffTrain_old(String arqArff, String model, int qtdeFrames){
		try{
			
			DataSource dsTrain = new DataSource(arqArff);
			
			List<String> lista1 = new ArrayList<String>();
			
			for(int i = 0; i < qtdeFrames; i++){
				lista1.add("2");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String strProb1 = insTrain.toString(1);
				lista1.add(strProb1);
					
			}
			for(int i = 0; i < qtdeFrames; i++){
				lista1.add("3");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
			
				System.out.println("ID: "+i);
				System.out.println("ID: "+dsTrain.getDataSet().numInstances());
				FileWriter arq = new FileWriter(model+".arff",true);
				PrintWriter gravarArq = new PrintWriter(arq);
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(7);
				System.out.println("classe "+classe);
					String str1 = "";
					
					for(int j = 0; j < ((qtdeFrames*2)+1); j++){
						str1 = str1 + lista1.get(i+j);
					}
					
					int frameBaseAlexnet = getBasePorno(str1,qtdeFrames);
					double probIntervalAlexnet = getProbabilityIntervalPorno(str1, qtdeFrames);
					double probPreviousAlexnet = getProbabilityPreviousPorno(str1, qtdeFrames);
					double probNextAlexnet = getProbabilityNextPorno(str1, qtdeFrames);
					int timePreviousAlexnet = getTimePreviousPorno(str1, qtdeFrames);
					int timeNextAlexnet = getTimeNextPorno(str1, qtdeFrames);
					
					String strArq = ""+frameBaseAlexnet+","+probIntervalAlexnet+","+probPreviousAlexnet+","+probNextAlexnet+","+timePreviousAlexnet+","+timeNextAlexnet+","+classe;

					gravarArq.println(strArq);
					arq.close();
					
					
			}
			
			System.out.println("Arquivo "+arqArff+" OK!!!");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public double formatDoubleDecimal(double num){
		
		String auxProb = String.format("%.5f", num);
		String resultProb = auxProb.replace(',','.');
		double res = Double.parseDouble(resultProb);
		return res;
		
	}
	
	public double getProbabilityIntervalPorno(String strArq, int qtdeFrames){
		
		int qdtePorno = 0;
		
		for(int i=0; i<strArq.length(); i++){  
            if(strArq.charAt(i) == '1'){ 
            	qdtePorno++;  
            }
		}

		
		double prob = formatDoubleDecimal((double)qdtePorno / ((qtdeFrames*2)+1));
		return prob;
	
	}
	
	public int getBasePorno(String strArq, int qtdeFrames){
		
		if(strArq.charAt(qtdeFrames) == '1'){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public double getProbabilityPreviousPorno(String strArq, int qtdeFrames){
		
		int qdtePorno = 0;
		
		for(int i=0; i < (qtdeFrames+1); i++){  
            if(strArq.charAt(i) == '1'){ 
            	qdtePorno++;  
            }
		}
		
		double prob = formatDoubleDecimal((double) qdtePorno / (qtdeFrames+1));
		return prob;
	}
	
	public double getProbabilityNextPorno(String strArq, int qtdeFrames){
		
		int qdtePorno = 0;
		
		for(int i= qtdeFrames; i< strArq.length(); i++){  
            if(strArq.charAt(i) == '1'){ 
            	qdtePorno++;  
            }
		}
		
		double prob = formatDoubleDecimal((double) qdtePorno / (qtdeFrames+1));
		return prob;
	}
	
	public int getTimeNextPorno(String strArq, int qtdeFrames){
		
		int timePorno = 0;

		for(int i = (qtdeFrames+1); i < strArq.length(); i++){  
			timePorno++;
			if(strArq.charAt(i) == '1'){ 

            	return timePorno;   
            }
		}
		
		return timePorno;
	}
	
	public int getTimePreviousPorno(String strArq, int qtdeFrames){
		
		int timePorno = 0;
		for(int i = (qtdeFrames-1); 0 <= i; i--){  
			timePorno++;
			if(strArq.charAt(i) == '1'){ 
            	return timePorno;   
            }
		}
		return timePorno;
	}
	
	public void loadFile(String path, String model, int qtdeFrames ){

		try{

			headArff(model,qtdeFrames);
		
			
			File file = new File(path);
			File afile[] = file.listFiles();
			int i = 0;
			for (int j = afile.length; i < j; i++) {
				File arquivos = afile[i];

				gerarArffTrain(path+arquivos.getName(),model,qtdeFrames);
			
			}
		}
		catch(Exception ex){
			
		}
	}
		
	public void headArff(String model, int qtdeFrames){
		try{
			FileWriter arq = new FileWriter(model+".arff",true);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.println("@relation "+model);
			gravarArq.println("");
			
			gravarArq.println("@attribute predict_base_alexnet numeric");
			gravarArq.println("@attribute probability_interval_alexnet numeric");
			gravarArq.println("@attribute probability_previous_alexnet numeric");
			gravarArq.println("@attribute probability_next_alexnet numeric");
			gravarArq.println("@attribute time_previous_alexnet numeric");
			gravarArq.println("@attribute time_next_alexnet numeric");
			
			gravarArq.println("@attribute predict_base_caffenet numeric");
			gravarArq.println("@attribute probability_interval_caffenet numeric");
			gravarArq.println("@attribute probability_previous_caffenet numeric");
			gravarArq.println("@attribute probability_next_caffenet numeric");
			gravarArq.println("@attribute time_previous_caffenet numeric");
			gravarArq.println("@attribute time_next_caffenet numeric");
			
			gravarArq.println("@attribute predict_base_googlenet numeric");
			gravarArq.println("@attribute probability_interval_googlenet numeric");
			gravarArq.println("@attribute probability_previous_googlenet numeric");
			gravarArq.println("@attribute probability_next_googlenet numeric");
			gravarArq.println("@attribute time_previous_googlenet numeric");
			gravarArq.println("@attribute time_next_googlenet numeric");
			
			gravarArq.println("@attribute predict_base_transfer_alexnet numeric");
			gravarArq.println("@attribute probability_interval_transfer_alexnet numeric");
			gravarArq.println("@attribute probability_previous_transfer_alexnet numeric");
			gravarArq.println("@attribute probability_next_transfer_alexnet numeric");
			gravarArq.println("@attribute time_previous_transfer_alexnet numeric");
			gravarArq.println("@attribute time_next_transfer_alexnet numeric");
			
			gravarArq.println("@attribute predict_base_transfer_caffenet numeric");
			gravarArq.println("@attribute probability_interval_transfer_caffenet numeric");
			gravarArq.println("@attribute probability_previous_transfer_caffenet numeric");
			gravarArq.println("@attribute probability_next_transfer_caffenet numeric");
			gravarArq.println("@attribute time_previous_transfer_caffenet numeric");
			gravarArq.println("@attribute time_next_transfer_caffenet numeric");
			
			gravarArq.println("@attribute predict_base_transfer_googlenet numeric");
			gravarArq.println("@attribute probability_interval_transfer_googlenet numeric");
			gravarArq.println("@attribute probability_previous_transfer_googlenet numeric");
			gravarArq.println("@attribute probability_next_transfer_googlenet numeric");
			gravarArq.println("@attribute time_previous_transfer_googlenet numeric");
			gravarArq.println("@attribute time_next_transfer_googlenet numeric");
			
			gravarArq.println("@attribute Class {0,1}");
			gravarArq.println("");
			gravarArq.println("@data");
			arq.close();

		}
		catch (Exception e) {
				// TODO: handle exception
		}
	}
}
