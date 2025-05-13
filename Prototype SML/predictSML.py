import weka.core.jvm as jvm
import weka.core.serialization as serialization
from weka.core.converters import Loader
from weka.classifiers import Classifier
from weka.core.dataset import Attribute, Instance, Instances

class Weka:
    def __init__(self, path_model="NaiveBayes.model"):
        self.path_model = path_model
        jvm.start()
    
    def createDataset(self):
        # create attributes
        predict_base = Attribute.create_numeric("predict_base_transfer_caffenet")
        probability_interval = Attribute.create_numeric("probability_interval_transfer_caffenet")
        probability_previous = Attribute.create_numeric("probability_previous_transfer_caffenet")
        probability_next = Attribute.create_numeric("probability_next_transfer_caffenet")
        time_previous = Attribute.create_numeric("time_previous_transfer_caffenet")
        time_next = Attribute.create_numeric("time_next_transfer_caffenet")
        Class =  Attribute.create_nominal("Class", ['0','1'])
        # create dataset
        dataset = Instances.create_instances("transfer_caffenet_test", [predict_base,probability_interval,probability_previous,\
                                                                        probability_next,time_previous,time_next,Class], 0)
        dataset.class_is_last()
        return dataset

    def loadModelClassifier(self):
        objects = serialization.read_all("NaiveBayes.model")
        classifier = Classifier(jobject=objects[0])
        loader = Loader(classname="weka.core.converters.ArffLoader")
        return classifier

    def predict(self,data, values):
        inst = Instance.create_instance(values)
        data.add_instance(inst)
        for inst in data:
            pred = classifier.classify_instance(inst)
            dist = classifier.distribution_for_instance(inst)
            self.printPredict(pred,dist)
            return pred, dist

    def printPredict(self,pred,dist):
        print("predict=" + str(pred) + ", probability=" + str(dist))

weka = Weka()
classifier = weka.loadModelClassifier()

data1 = weka.createDataset()
value1 = [1,0.33333,0.63636,0.09091,1,10,0]
pred,dist = weka.predict(data1,value1)

data2 = weka.createDataset()
value2 = [1,0.33333,0.54545,0.18182,1,1,0]
pred,dist = weka.predict(data2,value2)

jvm.stop()
