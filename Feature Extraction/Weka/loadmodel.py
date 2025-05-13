import weka.core.jvm as jvm
import weka.core.serialization as serialization
from weka.classifiers import Classifier
jvm.start()
objects = serialization.read_all("NaiveBayes.model")
classifier = Classifier(jobject=objects[0])
print(classifier)
jvm.stop()

