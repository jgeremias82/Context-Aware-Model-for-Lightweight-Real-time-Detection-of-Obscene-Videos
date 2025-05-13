import time

inicio = time.time()
import StackingMetaLearner_bench
fim = time.time()
benchmark = fim - inicio

inicio2 = time.time()
import StackingMetaLearner_binary
fim2 = time.time()
benchmark2 = fim2 - inicio2

print('Time Benchmark I: ',benchmark)
print('Time Benchmark II: ',benchmark2)

