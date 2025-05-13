import java.util.ArrayList;
import java.util.List;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

public class MontarArff {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MontarArff ma = new MontarArff();
		
		ma.visualizarArquivos("./arquivos2/caffenet/val/","ens_caffenet_val",100);
		/*ma.visualizarArquivos("./arquivos2/caffenet/train/","ens_caffenet_train",100);
		ma.visualizarArquivos("./arquivos2/caffenet/noporno/","ens_caffenet_noporno",100);
		ma.visualizarArquivos("./arquivos2/caffenet/porno/","ens_caffenet_porno",100);
		ma.visualizarArquivos("./arquivos2/caffenet/talvez/","ens_caffenet_talvez",100);*/
		
		System.out.println("All files process!!!");

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
				String strProb = insTrain.toString(1);
				lista.add(strProb);
					
			}
			for(int i = 0; i < qtdeFrames; i++){
				lista.add("3");
			}
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
		
				
				FileWriter arq = new FileWriter(model+".arff",true);
				PrintWriter gravarArq = new PrintWriter(arq);
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				String classe = insTrain.toString(2);
				
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
	
	public void visualizarArquivos(String path, String model, int qtdeFrames ){

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
			for(int i = 0; i < (qtdeFrames); i++){
				gravarArq.println("@attribute predict_previous"+(i+1)+" numeric");
			}
			
			gravarArq.println("@attribute predict_base numeric");
			
			for(int i = 0; i < (qtdeFrames); i++){
				gravarArq.println("@attribute predict_next"+(i+1)+" numeric");
			}
		
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
