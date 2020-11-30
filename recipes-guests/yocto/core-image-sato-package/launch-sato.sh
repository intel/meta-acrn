#!/bin/bash

offline_path="/sys/class/vhm/acrn_vhm"

# Check the device file of /dev/acrn_hsm to determine the offline_path
if [ -e "/dev/acrn_hsm" ]; then
offline_path="/sys/class/acrn/acrn_hsm"
fi

function launch_UOS()
{
mac=$(cat /sys/class/net/e*/address)
vm_name=vm$1
mac_seed=${mac:9:8}-${vm_name}

#check if the vm is running or not
vm_ps=$(pgrep -a -f acrn-dm)
result=$(echo $vm_ps | grep -w "${vm_name}")
if [[ "$result" != "" ]]; then
  echo "$vm_name is running, can't create twice!"
  exit
fi

#logger_setting, format: logger_name,level; like following
logger_setting="--logger_setting console,level=4;kmsg,level=3;disk,level=5"

#for pm by vuart setting
pm_channel="--pm_notify_channel uart "
pm_by_vuart="--pm_by_vuart pty,/run/acrn/life_mngr_"$vm_name
pm_vuart_node=" -s 1:0,lpc -l com2,/run/acrn/life_mngr_"$vm_name

#for memsize setting
mem_size=2048M

gpudevice=`cat /sys/bus/pci/devices/0000:00:02.0/device`

echo "8086 $gpudevice" > /sys/bus/pci/drivers/pci-stub/new_id
echo "0000:00:02.0" > /sys/bus/pci/devices/0000:00:02.0/driver/unbind
echo "0000:00:02.0" > /sys/bus/pci/drivers/pci-stub/bind

acrn-dm -A -m $mem_size  -s 0:0,hostbridge \
  -s 2,passthru,0/2/0,gpu \
  -s 5,virtio-console,@stdio:stdio_port \
  -s 6,virtio-hyper_dmabuf \
  -s 3,virtio-blk,/var/lib/machines/core-image-sato.wic \
  -s 4,virtio-net,tap0 \
  -s 7,virtio-rnd \
  --ovmf /usr/share/acrn/bios/OVMF.fd \
  $pm_channel $pm_by_vuart $pm_vuart_node \
  $logger_setting \
  --mac_seed $mac_seed \
  $vm_name
}

# offline SOS CPUs except BSP before launch UOS
for i in `ls -d /sys/devices/system/cpu/cpu[1-99]`; do
        online=`cat $i/online`
        idx=`echo $i | tr -cd "[1-99]"`
        echo cpu$idx online=$online
        if [ "$online" = "1" ]; then
                echo 0 > $i/online
		# during boot time, cpu hotplug may be disabled by pci_device_probe during a pci module insmod
		while [ "$online" = "1" ]; do
			sleep 1
			echo 0 > $i/online
			online=`cat $i/online`
		done
                echo $idx > ${offline_path}/offline_cpu
        fi
done

launch_UOS 1 "64 448 8" 
