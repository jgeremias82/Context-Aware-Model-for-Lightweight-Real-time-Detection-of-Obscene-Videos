import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

public class ContadorManipularArff {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ContadorManipularArff ma = new ContadorManipularArff();
		//ma.visualizarArquivos("train","./arquivos/juca/train/","juca_train",5);
		/*ma.visualizarArquivos("train","./arquivos/alexnet/train/","alexnet_train",50);
		ma.visualizarArquivos("train","./arquivos/caffenet/train/","caffenet_train",50);
		ma.visualizarArquivos("train","./arquivos/googlenet/train/","googlenet_train",50);
		ma.visualizarArquivos("train","./arquivos/transfer_alexnet/train/","transfer_alexnet_train",50);
		ma.visualizarArquivos("train","./arquivos/transfer_caffenet/train/","transfer_caffenet_train",50);
		ma.visualizarArquivos("train","./arquivos/transfer_googlenet/train/","transfer_googlenet_train",50);*/
		
		//ma.visualizarArquivos("test","./arquivos/juca/train/","juca_test",5);
		ma.visualizarArquivos("test","./arquivos/alexnet/test/","alexnet_test",50);
		ma.visualizarArquivos("test","./arquivos/caffenet/test/","caffenet_test",50);
		ma.visualizarArquivos("test","./arquivos/googlenet/test/","googlenet_test",50);
		ma.visualizarArquivos("test","./arquivos/transfer_alexnet/test/","transfer_alexnet_test",50);
		ma.visualizarArquivos("test","./arquivos/transfer_caffenet/test/","transfer_caffenet_test",50);
		ma.visualizarArquivos("test","./arquivos/transfer_googlenet/test/","transfer_googlenet_test",50);
		
		System.out.println("All files process!!!");

	}
	public void gerarArffTest(String arqArff, String model, int qtdeFrames){
		try{
			
			DataSource dsTrain = new DataSource(arqArff);
			
			List<String> lista = new ArrayList<String>();
			for(int i = 0; i < qtdeFrames; i++){
				lista.add("2");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String strProb = insTrain.toString(2);
				lista.add(strProb);
					
			}
			for(int i = 0; i < qtdeFrames; i++){
				lista.add("3");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				
				FileWriter arqNoPorno = new FileWriter(model+"_noporno.arff",true);
				PrintWriter gravarArqNoPorno = new PrintWriter(arqNoPorno);
				
				FileWriter arqPorno = new FileWriter(model+"_porno.arff",true);
				PrintWriter gravarArqPorno = new PrintWriter(arqPorno);
				
				FileWriter arqTalvez = new FileWriter(model+"_talvez.arff",true);
				PrintWriter gravarArqTalvez = new PrintWriter(arqTalvez);
				
				FileWriter arqTalvez2 = new FileWriter(model+"_talvez2.arff",true);
				PrintWriter gravarArqTalvez2 = new PrintWriter(arqTalvez2);
				
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(3);
				
				if(classe.equals("0")){
					String strArqNoPorno = "";
					for(int j = 0; j < ((qtdeFrames*2)+1); j++){
						
						strArqNoPorno = strArqNoPorno + lista.get(i+j)+",";
					}
					strArqNoPorno = strArqNoPorno + classe;
					gravarArqNoPorno.println(strArqNoPorno);
				}
				if(classe.equals("1")){
					String strArqPorno = "";
					for(int j = 0; j < ((qtdeFrames*2)+1); j++){
						
						strArqPorno = strArqPorno + lista.get(i+j)+",";
					}
					strArqPorno = strArqPorno + classe;
					gravarArqPorno.println(strArqPorno);

				}
				if(classe.equals("2")){
					String strArqTalvez = "";
					for(int j = 0; j < ((qtdeFrames*2)+1); j++){
						
						strArqTalvez = strArqTalvez + lista.get(i+j)+",";
					}
					strArqTalvez = strArqTalvez + "0";
					if (i % 2 == 0) {
						gravarArqTalvez.println(strArqTalvez);  
					}else {
						gravarArqTalvez2.println(strArqTalvez);  
					}
					
				}
				
				
				
					/*String strArq = "";
					for(int j = 0; j < ((qtdeFrames*2)+1); j++){
						
						strArq = strArq + lista.get(i+j)+",";
					}
				
					strArq = strArq + classe;

					gravarArq.println(strArq);
					arq.close();*/
					arqNoPorno.close();
					arqPorno.close();
					arqTalvez.close();
					arqTalvez2.close();
					System.out.println("Arquivo "+arqArff+" OK!!!");
			}
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void gerarArffTrain(String arqArff, String model, int qtdeFrames){
		try{
			
			DataSource dsTrain = new DataSource(arqArff);
			
			List<String> lista = new ArrayList<String>();
			for(int i = 0; i < qtdeFrames; i++){
				lista.add("2");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String strProb = insTrain.toString(2);
				lista.add(strProb);
					
			}
			for(int i = 0; i < qtdeFrames; i++){
				lista.add("3");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
		
				
				FileWriter arq = new FileWriter(model+".arff",true);
				PrintWriter gravarArq = new PrintWriter(arq);
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(3);
				
					String strArq = "";
					for(int j = 0; j < ((qtdeFrames*2)+1); j++){
						
						strArq = strArq + lista.get(i+j)+",";
					}
				
					strArq = strArq + classe;

					gravarArq.println(strArq);
					arq.close();
					System.out.println("Arquivo "+arqArff+" OK!!!");
			}
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void visualizarArquivosOld(String etapa, String path, String model, int qtdeFrames ){

		try{
			
			headArff(etapa, model,qtdeFrames);
		
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
	
	public void visualizarArquivos(String etapa, String path, String model, int qtdeFrames ){

		try{
			
			if(etapa.equals("train")){
				headArff(etapa, model,qtdeFrames);
			}
			else{
				headArff(etapa, model+"_noporno",qtdeFrames);
				headArff(etapa, model+"_porno",qtdeFrames);
				headArff(etapa, model+"_talvez",qtdeFrames);
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
		
	public void headArff(String etapa, String model, int qtdeFrames){
		try{
			FileWriter arq = new FileWriter(model+".arff",true);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.println("@relation "+model);
			gravarArq.println("");
			for(int i = 0; i < (qtdeFrames); i++){
				gravarArq.println("@attribute predict_previous"+(i+1)+" numeric");
			}
			
			gravarArq.println("@attribute predict_base numeric");
			
			for(int i = 0; i < (qtdeFrames); i++){
				gravarArq.println("@attribute predict_next"+(i+1)+" numeric");
			}
			
			if(etapa.equals("train")){
				gravarArq.println("@attribute Class {0,1}");
			}
			else{
				gravarArq.println("@attribute Class {0,1}");
			}
			
			gravarArq.println("");
			gravarArq.println("@data");
			arq.close();

		}
		catch (Exception e) {
				// TODO: handle exception
		}
	}
}
