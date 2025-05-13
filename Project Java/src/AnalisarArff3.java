import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

public class AnalisarArff3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AnalisarArff3 ma = new AnalisarArff3();
		
		//ma.loadFile("./arquivos3/noporno/","ens_all_noporno_new9",9);
		//ma.loadFile("./arquivos3/porno/","ens_all_porno_new9",9);
		ma.loadFile("./arquivos3/talvez/","ens_all_talvez_new9",9);
		//ma.loadFile("./arquivos3/train/","ens_all_train_new9",9);
		//ma.loadFile("./arquivos3/val/","ens_all_val_new9",9);
		
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
	
	public String getStringList(DataSource dsTrain, int qtdeFrames, List<String> list, int instancia){
		try{
			
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
			
			List<String> list1 = getList(dsTrain, qtdeFrames,1);
			List<String> list2 = getList(dsTrain, qtdeFrames,2);
			List<String> list3 = getList(dsTrain, qtdeFrames,3);
			List<String> list4 = getList(dsTrain, qtdeFrames,4);
			List<String> list5 = getList(dsTrain, qtdeFrames,5);
			List<String> list6 = getList(dsTrain, qtdeFrames,6);
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				
				FileWriter arq1 = new FileWriter("alexnet_grafico.arff",true);
				PrintWriter gravarArq1 = new PrintWriter(arq1);
				
				FileWriter arq2 = new FileWriter("caffenet_grafico.arff",true);
				PrintWriter gravarArq2 = new PrintWriter(arq2);
				
				FileWriter arq3 = new FileWriter("googlenet_grafico.arff",true);
				PrintWriter gravarArq3 = new PrintWriter(arq3);
				
				FileWriter arq4 = new FileWriter("transfer_alexnet_grafico.arff",true);
				PrintWriter gravarArq4 = new PrintWriter(arq4);
				
				FileWriter arq5 = new FileWriter("transfer_caffenet_grafico.arff",true);
				PrintWriter gravarArq5 = new PrintWriter(arq5);
				
				FileWriter arq6 = new FileWriter("transfer_googlenet_grafico.arff",true);
				PrintWriter gravarArq6 = new PrintWriter(arq6);
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(7);
	
				String strAlexnet = getStringList(dsTrain, qtdeFrames,list1,i);
				String strCaffenet = getStringList(dsTrain, qtdeFrames,list2,i);
				String strGooglenet = getStringList(dsTrain, qtdeFrames,list3,i);
				String strTrasferAlexnet = getStringList(dsTrain, qtdeFrames,list4,i);
				String strTransferCaffenet = getStringList(dsTrain, qtdeFrames,list5,i);
				String strTransferGooglenet = getStringList(dsTrain, qtdeFrames,list6,i);
				
				String strArq1 = strAlexnet + classe;
				String strArq2 = strCaffenet + classe;
				String strArq3 = strGooglenet + classe;
				String strArq4 = strTrasferAlexnet + classe;
				String strArq5 = strTransferCaffenet + classe;
				String strArq6 = strTransferGooglenet + classe;
				
				gravarArq1.println(strArq1);
				arq1.close();
				
				gravarArq2.println(strArq2);
				arq2.close();
				
				gravarArq3.println(strArq3);
				arq3.close();
				
				gravarArq4.println(strArq4);
				arq4.close();
				
				gravarArq5.println(strArq5);
				arq5.close();
				
				gravarArq6.println(strArq6);
				arq6.close();
					
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
	
	public void loadFile(String path, String model, int qtdeFrames){

		try{

			//headArff(model,qtdeFrames);
			
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
			FileWriter arq1 = new FileWriter("alexnet_grafico.arff",true);
			PrintWriter gravarArq1 = new PrintWriter(arq1);
			gravarArq1.println("@relation alexnet_grafico");
			gravarArq1.println("");
			
			FileWriter arq2 = new FileWriter("caffenet_grafico.arff",true);
			PrintWriter gravarArq2 = new PrintWriter(arq2);
			gravarArq2.println("@relation caffenet_grafico");
			gravarArq2.println("");
			
			FileWriter arq3 = new FileWriter("googlenet_grafico.arff",true);
			PrintWriter gravarArq3 = new PrintWriter(arq3);
			gravarArq3.println("@relation googlenet_grafico");
			gravarArq3.println("");
			
			FileWriter arq4 = new FileWriter("transfer_alexnet_grafico.arff",true);
			PrintWriter gravarArq4 = new PrintWriter(arq4);
			gravarArq4.println("@relation transfer_alexnet_grafico");
			gravarArq4.println("");
			
			FileWriter arq5 = new FileWriter("transfer_caffenet_grafico.arff",true);
			PrintWriter gravarArq5 = new PrintWriter(arq5);
			gravarArq5.println("@relation transfer_caffenet_grafico");
			gravarArq5.println("");
			
			FileWriter arq6 = new FileWriter("transfer_googlenet_grafico.arff",true);
			PrintWriter gravarArq6 = new PrintWriter(arq6);
			gravarArq6.println("@relation transfer_googlenet_grafico");
			gravarArq6.println("");
			
			/*FileWriter arq = new FileWriter(model+".arff",true);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.println("@relation "+model);
			gravarArq.println("");*/
			
			gravarArq1.println("@attribute predict_base_alexnet numeric");
			gravarArq1.println("@attribute probability_interval_alexnet numeric");
			gravarArq1.println("@attribute probability_previous_alexnet numeric");
			gravarArq1.println("@attribute probability_next_alexnet numeric");
			gravarArq1.println("@attribute time_previous_alexnet numeric");
			gravarArq1.println("@attribute time_next_alexnet numeric");
			
			gravarArq2.println("@attribute predict_base_caffenet numeric");
			gravarArq2.println("@attribute probability_interval_caffenet numeric");
			gravarArq2.println("@attribute probability_previous_caffenet numeric");
			gravarArq2.println("@attribute probability_next_caffenet numeric");
			gravarArq2.println("@attribute time_previous_caffenet numeric");
			gravarArq2.println("@attribute time_next_caffenet numeric");
			
			gravarArq3.println("@attribute predict_base_googlenet numeric");
			gravarArq3.println("@attribute probability_interval_googlenet numeric");
			gravarArq3.println("@attribute probability_previous_googlenet numeric");
			gravarArq3.println("@attribute probability_next_googlenet numeric");
			gravarArq3.println("@attribute time_previous_googlenet numeric");
			gravarArq3.println("@attribute time_next_googlenet numeric");
			
			gravarArq4.println("@attribute predict_base_transfer_alexnet numeric");
			gravarArq4.println("@attribute probability_interval_transfer_alexnet numeric");
			gravarArq4.println("@attribute probability_previous_transfer_alexnet numeric");
			gravarArq4.println("@attribute probability_next_transfer_alexnet numeric");
			gravarArq4.println("@attribute time_previous_transfer_alexnet numeric");
			gravarArq4.println("@attribute time_next_transfer_alexnet numeric");
			
			gravarArq5.println("@attribute predict_base_transfer_caffenet numeric");
			gravarArq5.println("@attribute probability_interval_transfer_caffenet numeric");
			gravarArq5.println("@attribute probability_previous_transfer_caffenet numeric");
			gravarArq5.println("@attribute probability_next_transfer_caffenet numeric");
			gravarArq5.println("@attribute time_previous_transfer_caffenet numeric");
			gravarArq5.println("@attribute time_next_transfer_caffenet numeric");
			
			gravarArq6.println("@attribute predict_base_transfer_googlenet numeric");
			gravarArq6.println("@attribute probability_interval_transfer_googlenet numeric");
			gravarArq6.println("@attribute probability_previous_transfer_googlenet numeric");
			gravarArq6.println("@attribute probability_next_transfer_googlenet numeric");
			gravarArq6.println("@attribute time_previous_transfer_googlenet numeric");
			gravarArq6.println("@attribute time_next_transfer_googlenet numeric");
			
			gravarArq1.println("@attribute Class {0,1}");
			gravarArq1.println("");
			gravarArq1.println("@data");
			arq1.close();
			
			gravarArq2.println("@attribute Class {0,1}");
			gravarArq2.println("");
			gravarArq2.println("@data");
			arq2.close();
			
			gravarArq3.println("@attribute Class {0,1}");
			gravarArq3.println("");
			gravarArq3.println("@data");
			arq3.close();
			
			gravarArq4.println("@attribute Class {0,1}");
			gravarArq4.println("");
			gravarArq4.println("@data");
			arq4.close();
			
			gravarArq5.println("@attribute Class {0,1}");
			gravarArq5.println("");
			gravarArq5.println("@data");
			arq5.close();
			
			gravarArq6.println("@attribute Class {0,1}");
			gravarArq6.println("");
			gravarArq6.println("@data");
			arq6.close();

		}
		catch (Exception e) {
				// TODO: handle exception
		}
	}
}
