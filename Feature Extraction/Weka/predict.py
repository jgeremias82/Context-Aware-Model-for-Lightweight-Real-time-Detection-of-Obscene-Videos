import weka.core.jvm as jvm
import weka.core.serialization as serialization
from weka.core.converters import Loader
from weka.classifiers import Classifier

jvm.start()
objects = serialization.read_all("NaiveBayes.model")
classifier = Classifier(jobject=objects[0])
#print(classifier)

loader = Loader(classname="weka.core.converters.ArffLoader")
data = loader.load_file('transfer_caffenet_test.arff')
data.class_is_last()
    
for index, inst in enumerate(data):
    pred = classifier.classify_instance(inst)
    dist = classifier.distribution_for_instance(inst)
    print int(pred)
jvm.stop()
