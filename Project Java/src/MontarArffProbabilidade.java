import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

public class MontarArffProbabilidade {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MontarArffProbabilidade ma = new MontarArffProbabilidade();
		
		/*ma.visualizarArquivos("./arquivos4/alexnet/arff/noporno/","ind_graf_alexnet_union",1);
		ma.visualizarArquivos("./arquivos4/alexnet/arff/porno/","ind_graf_alexnet_union",0);
		ma.visualizarArquivos("./arquivos4/alexnet/arff/talvez/","ind_graf_alexnet_union",0);
		ma.visualizarArquivos("./arquivos4/alexnet/arff/val/","ind_graf_alexnet_val",1);
		ma.visualizarArquivos("./arquivos4/alexnet/arff/train/","ind_graf_alexnet_train",1);
		
		ma.visualizarArquivos("./arquivos4/caffenet/arff/noporno/","ind_graf_caffenet_union",1);
		ma.visualizarArquivos("./arquivos4/caffenet/arff/porno/","ind_graf_caffenet_union",0);
		ma.visualizarArquivos("./arquivos4/caffenet/arff/talvez/","ind_graf_caffenet_union",0);
		ma.visualizarArquivos("./arquivos4/caffenet/arff/val/","ind_graf_caffenet_val",1);
		ma.visualizarArquivos("./arquivos4/caffenet/arff/train/","ind_graf_caffenet_train",1);
		
		ma.visualizarArquivos("./arquivos4/googlenet/arff/noporno/","ind_graf_googlenet_union",1);
		ma.visualizarArquivos("./arquivos4/googlenet/arff/porno/","ind_graf_googlenet_union",0);
		ma.visualizarArquivos("./arquivos4/googlenet/arff/talvez/","ind_graf_googlenet_union",0);
		ma.visualizarArquivos("./arquivos4/googlenet/arff/val/","ind_graf_googlenet_val",1);
		ma.visualizarArquivos("./arquivos4/googlenet/arff/train/","ind_graf_googlenet_train",1);
		
		ma.visualizarArquivos("./arquivos4/transfer_alexnet/arff/noporno/","ind_graf_transfer_alexnet_union",1);
		ma.visualizarArquivos("./arquivos4/transfer_alexnet/arff/porno/","ind_graf_transfer_alexnet_union",0);
		ma.visualizarArquivos("./arquivos4/transfer_alexnet/arff/talvez/","ind_graf_transfer_alexnet_union",0);
		ma.visualizarArquivos("./arquivos4/transfer_alexnet/arff/val/","ind_graf_transfer_alexnet_val",1);
		ma.visualizarArquivos("./arquivos4/transfer_alexnet/arff/train/","ind_graf_transfer_alexnet_train",1);
		
		ma.visualizarArquivos("./arquivos4/transfer_caffenet/arff/noporno/","ind_graf_transfer_caffenet_union",1);
		ma.visualizarArquivos("./arquivos4/transfer_caffenet/arff/porno/","ind_graf_transfer_caffenet_union",0);
		ma.visualizarArquivos("./arquivos4/transfer_caffenet/arff/talvez/","ind_graf_transfer_caffenet_union",0);
		ma.visualizarArquivos("./arquivos4/transfer_caffenet/arff/val/","ind_graf_transfer_caffenet_val",1);
		ma.visualizarArquivos("./arquivos4/transfer_caffenet/arff/train/","ind_graf_transfer_caffenet_train",1);
		*/
		ma.visualizarArquivos("./arquivos4/transfer_googlenet/arff/noporno/","ind_graf_transfer_googlenet_union",1);
		ma.visualizarArquivos("./arquivos4/transfer_googlenet/arff/porno/","ind_graf_transfer_googlenet_union",0);
		ma.visualizarArquivos("./arquivos4/transfer_googlenet/arff/talvez/","ind_graf_transfer_googlenet_union",0);
		ma.visualizarArquivos("./arquivos4/transfer_googlenet/arff/val/","ind_graf_transfer_googlenet_val",1);
		ma.visualizarArquivos("./arquivos4/transfer_googlenet/arff/train/","ind_graf_transfer_googlenet_train",1);
		
		System.out.println("All files process!!!");

	}

	public void gerarArffTrain(String arqArff, String model){
		try{
			
			DataSource dsTrain = new DataSource(arqArff);
			
			for(int i = 0; i < dsTrain.getDataSet().numInstances(); i++){
				
				Instance insTrain = dsTrain.getDataSet().instance(i);
				
				//String strFrame = insTrain.toString(0);
				String strPredict = insTrain.toString(1);
				String strProb = insTrain.toString(2);
				String classe = insTrain.toString(3);
				
				FileWriter arq = new FileWriter(model+".arff",true);
				PrintWriter gravarArq = new PrintWriter(arq);
				
				String strArq = strPredict+","+strProb+","+classe;
				
				gravarArq.println(strArq);
				arq.close();
				System.out.println("Arquivo "+arqArff+" OK!!!");
					
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void visualizarArquivos(String path, String model, int head){

		try{
			

			if(head == 1){
				headArff(model);
			}
			
			File file = new File(path);
			File afile[] = file.listFiles();
			int i = 0;
			for (int j = afile.length; i < j; i++) {
				File arquivos = afile[i];

				gerarArffTrain(path+arquivos.getName(),model);
			
			}
		}
		catch(Exception ex){
			
		}
	}
		
	public void headArff(String model){
		try{
			FileWriter arq = new FileWriter(model+".arff",true);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.println("@relation "+model);
			gravarArq.println("");
			//gravarArq.println("@attribute frame numeric");
			gravarArq.println("@attribute predict numeric");
			gravarArq.println("@attribute probability numeric");
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
