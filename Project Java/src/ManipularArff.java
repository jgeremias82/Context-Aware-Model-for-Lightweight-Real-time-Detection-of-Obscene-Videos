import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

public class ManipularArff {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ManipularArff ma = new ManipularArff();
		ma.visualizarArquivos("train","./arquivos/alexnet/train/","alexnet_train",50);
		ma.visualizarArquivos("train","./arquivos/caffenet/train/","caffenet_train",50);
		ma.visualizarArquivos("train","./arquivos/googlenet/train/","googlenet_train",50);
		ma.visualizarArquivos("train","./arquivos/transfer_alexnet/train/","transfer_alexnet_train",50);
		ma.visualizarArquivos("train","./arquivos/transfer_caffenet/train/","transfer_caffenet_train",50);
		ma.visualizarArquivos("train","./arquivos/transfer_googlenet/train/","transfer_googlenet_train",50);
		
		ma.visualizarArquivos("test","./arquivos/alexnet/test/","alexnet_test",50);
		ma.visualizarArquivos("test","./arquivos/caffenet/test/","caffenet_test",50);
		ma.visualizarArquivos("test","./arquivos/googlenet/test/","googlenet_test",50);
		ma.visualizarArquivos("test","./arquivos/transfer_alexnet/test/","transfer_alexnet_test",50);
		ma.visualizarArquivos("test","./arquivos/transfer_caffenet/test/","transfer_caffenet_test",50);
		ma.visualizarArquivos("test","./arquivos/transfer_googlenet/test/","transfer_googlenet_test",50);
		
		System.out.println("All files process!!!");

	}
	public void gerarArff_old(String arqArff, String model){
		try{
			
			DataSource dsTrain = new DataSource(arqArff);
			
			List<String> lista = new ArrayList<String>();
			for(int i = 0; i < 5; i++){
				lista.add("0.000000");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				Instance insTrain = dsTrain.getDataSet().instance(i);
				
				if(insTrain.toString(2).equals("1")){
					//lista.add(insTrain.toString(1));
					String strProb = insTrain.toString(1);
					Double prob = Double.parseDouble(strProb);
					String auxProb = String.format("%.6f", prob);
					String resultProb = auxProb.replace(',','.');
					lista.add(resultProb);
				}
				else{
					
					String strProb = insTrain.toString(1);
					Double prob = 1 - Double.parseDouble(strProb);
					String auxProb = String.format("%.6f", prob);
					String resultProb = auxProb.replace(',','.');
					lista.add(resultProb);
					
				}
					
			}
			
			FileWriter arq = new FileWriter(model+".arff",true);
			PrintWriter gravarArq = new PrintWriter(arq);
			
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(3);
				
				gravarArq.println(lista.get(i)+","+lista.get(i+1)+","+lista.get(i+2)+","+lista.get(i+3)+","+lista.get(i+4)+","+classe);
			 
			}
			
			arq.close();
		
			System.out.println("Arquivo "+arqArff+" OK!!!");
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void gerarArffTest(String arqArff, String model, int qtdeFrames){
		try{
			
			DataSource dsTrain = new DataSource(arqArff);
			
			List<String> lista = new ArrayList<String>();
			for(int i = 0; i < (qtdeFrames -1); i++){
				lista.add("0.000000");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				Instance insTrain = dsTrain.getDataSet().instance(i);
				
				if(insTrain.toString(2).equals("1")){
					//lista.add(insTrain.toString(1));
					String strProb = insTrain.toString(1);
					Double prob = Double.parseDouble(strProb);
					String auxProb = String.format("%.6f", prob);
					String resultProb = auxProb.replace(',','.');
					lista.add(resultProb);
				}
				else{
					
					String strProb = insTrain.toString(1);
					Double prob = 1 - Double.parseDouble(strProb);
					String auxProb = String.format("%.6f", prob);
					String resultProb = auxProb.replace(',','.');
					lista.add(resultProb);
					
				}
					
			}
			
			FileWriter arqNoPorno = new FileWriter(model+"_noporno.arff",true);
			PrintWriter gravarArqNoPorno = new PrintWriter(arqNoPorno);
			
			FileWriter arqPorno = new FileWriter(model+"_porno.arff",true);
			PrintWriter gravarArqPorno = new PrintWriter(arqPorno);
			
			FileWriter arqTalvez = new FileWriter(model+"_talvez.arff",true);
			PrintWriter gravarArqTalvez = new PrintWriter(arqTalvez);
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(3);
				
				if(classe.equals("0")){
					String strArqNoPorno = "";
					for(int j = 0; j < qtdeFrames; j++){
						
						strArqNoPorno = strArqNoPorno + lista.get(i+j)+",";
					}
					strArqNoPorno = strArqNoPorno + classe;
					gravarArqNoPorno.println(strArqNoPorno);
				}
				if(classe.equals("1")){
					String strArqPorno = "";
					for(int j = 0; j < qtdeFrames; j++){
						
						strArqPorno = strArqPorno + lista.get(i+j)+",";
					}
					strArqPorno = strArqPorno + classe;
					gravarArqPorno.println(strArqPorno);

				}
				if(classe.equals("2")){
					String strArqTalvez = "";
					for(int j = 0; j < qtdeFrames; j++){
						
						strArqTalvez = strArqTalvez + lista.get(i+j)+",";
					}
					strArqTalvez = strArqTalvez + classe;
					gravarArqTalvez.println(strArqTalvez);
				}
				 
			}
			
			arqNoPorno.close();
			arqPorno.close();
			arqTalvez.close();
			
			System.out.println("Arquivo "+arqArff+" OK!!!");
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void gerarArffTrain(String arqArff, String model, int qtdeFrames){
		try{
			
			DataSource dsTrain = new DataSource(arqArff);
			
			List<String> lista = new ArrayList<String>();
			for(int i = 0; i < (qtdeFrames -1); i++){
				lista.add("0.000000");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				Instance insTrain = dsTrain.getDataSet().instance(i);
				
				if(insTrain.toString(2).equals("1")){
					//lista.add(insTrain.toString(1));
					String strProb = insTrain.toString(1);
					Double prob = Double.parseDouble(strProb);
					String auxProb = String.format("%.6f", prob);
					String resultProb = auxProb.replace(',','.');
					lista.add(resultProb);
				}
				else{
					
					String strProb = insTrain.toString(1);
					Double prob = 1 - Double.parseDouble(strProb);
					String auxProb = String.format("%.6f", prob);
					String resultProb = auxProb.replace(',','.');
					lista.add(resultProb);
					
				}
					
			}
			
			FileWriter arq = new FileWriter(model+".arff",true);
			PrintWriter gravarArq = new PrintWriter(arq);
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(3);
				
					String strArq = "";
					for(int j = 0; j < qtdeFrames; j++){
						
						strArq = strArq + lista.get(i+j)+",";
					}
					strArq = strArq + classe;
					gravarArq.println(strArq);
							 
			}
			
			arq.close();
			System.out.println("Arquivo "+arqArff+" OK!!!");
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void visualizarArquivos(String etapa, String path, String model, int qtdeFrames ){

		try{
			
			if(etapa.equals("train")){
				headArff(model,qtdeFrames);
			}
			else{
				headArff(model+"_noporno",qtdeFrames);
				headArff(model+"_porno",qtdeFrames);
				headArff(model+"_talvez",qtdeFrames);
			}
			
			File file = new File(path);
			File afile[] = file.listFiles();
			int i = 0;
			for (int j = afile.length; i < j; i++) {
				File arquivos = afile[i];
				//System.out.println(arquivos.getName());
				if(etapa.equals("train")){
					gerarArffTrain(path+arquivos.getName(),model,qtdeFrames);
				}
				else{
					gerarArffTest(path+arquivos.getName(),model,qtdeFrames);
				}
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
			for(int i = 0; i < qtdeFrames; i++){
				gravarArq.println("@attribute probability"+(i+1)+" numeric");
			}
			
			gravarArq.println("@attribute Class {0,1,2}");
			gravarArq.println("");
			gravarArq.println("@data");
			arq.close();

		}
		catch (Exception e) {
				// TODO: handle exception
		}
	}
}
