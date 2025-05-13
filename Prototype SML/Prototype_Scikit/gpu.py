import psutil
import platform
from datetime import datetime
import time
import GPUtil
from tabulate import tabulate

check = True
cont = 0 
while(check):
    #print("="*40, "GPU Details", "="*40)
    gpus = GPUtil.getGPUs()
    list_gpus = []
    for gpu in gpus:
        # get the GPU id
        gpu_id = gpu.id
        # name of GPU
        gpu_name = gpu.name
        # get % percentage of GPU usage of that GPU
        gpu_load = (gpu.load*100)
        # get free memory in MB format
        gpu_free_memory = gpu.memoryFree
        # get used memory
        gpu_used_memory = gpu.memoryUsed
        # get total memory
        gpu_total_memory = gpu.memoryTotal
        # get GPU temperature in Celsius
        gpu_temperature = gpu.temperature
        gpu_uuid = gpu.uuid
        list_gpus.append((
            gpu_id, gpu_name, gpu_load, gpu_free_memory, gpu_used_memory,
            gpu_total_memory, gpu_temperature, gpu_uuid
        ))

    print tabulate(list_gpus, headers=("id", "name", "load", "free memory", "used memory", "total memory", "temperature", "uuid"))
    if cont == 30:
        check = False
    cont +=1
