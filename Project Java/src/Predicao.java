import weka.core.Instance;

public class Predicao implements Comparable<Predicao>{
	
	private int idFrame;
	private double classe;
	private double probability;
	private Instance instancia;
	public int getIdFrame() {
		return idFrame;
	}
	public void setIdFrame(int idFrame) {
		this.idFrame = idFrame;
	}
	public double getClasse() {
		return classe;
	}
	public void setClasse(double classe) {
		this.classe = classe;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
	public Instance getInstancia() {
		return instancia;
	}
	public void setInstancia(Instance instancia) {
		this.instancia = instancia;
	}
	public int compareTo(Predicao predicao) {
		if (this.probability > predicao.probability) {
            return -1;
        }
        if (this.probability < predicao.probability) {
            return 1;
        }
        return 0; 
	}

}
