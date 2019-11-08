#!/bin/bash

offline_path="/sys/class/vhm/acrn_vhm"

# Check the device file of /dev/acrn_hsm to determine the offline_path
if [ -e "/dev/acrn_hsm" ]; then
offline_path="/sys/class/acrn/acrn_hsm"
fi

function launch_clear()
{
mac=$(cat /sys/class/net/e*/address)
vm_name=vm$1
mac_seed=${mac:9:8}-${vm_name}

#check if the vm is running or not
vm_ps=$(pgrep -a -f acrn-dm)
result=$(echo $vm_ps | grep "${vm_name}")
if [[ "$result" != "" ]]; then
  echo "$vm_name is running, can't create twice!"
  exit
fi

#for memsize setting
mem_size=2048M

acrn-dm -A -m $mem_size -c $2 -s 0:0,hostbridge -s 1:0,lpc -l com1,stdio \
  -s 5,virtio-console,@pty:pty_port \
  -s 6,virtio-hyper_dmabuf \
  -s 3,virtio-blk,/var/lib/machines/core-image-base.wic \
  -s 4,virtio-net,tap0 \
  --ovmf /usr/share/acrn/bios/OVMF.fd \
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

launch_clear 1 1 "64 448 8" 0x070F00
