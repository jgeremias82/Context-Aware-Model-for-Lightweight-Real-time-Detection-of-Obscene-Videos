import time

#gc.collect() 
#time.sleep(3)

#inicio = time.time()
#import StackingMetaLearner_Bench_CPU
#fim = time.time()
#benchmark = fim - inicio

#gc.collect() 
#time.sleep(3)

#inicio2 = time.time()
#import StackingMetaLearner_Bench_GPU
#fim2 = time.time()
#benchmark2 = fim2 - inicio2

#gc.collect() 
#time.sleep(3)

#inicio3 = time.time()
#import StackingMetaLearner_Binary_CPU
#fim3 = time.time()
#benchmark3 = fim3 - inicio3

#gc.collect() 
#time.sleep(3)

inicio4 = time.time()
import StackingMetaLearner_Binary_GPU
fim4 = time.time()
benchmark4 = fim4 - inicio4

#gc.collect() 
#time.sleep(3)

#print('Interpretado CPU: ',benchmark)
#print('Interpretado GPU: ',benchmark2)
#print('Binary CPU: ',benchmark3)
print('Binary GPU: ',benchmark4)
#print(str(benchmark)+';'+str(benchmark2)+';'+str(benchmark3)+';'+str(benchmark4))
