SUMMARY = "Linux Kernel with ACRN enabled"

SRC_URI_append = "  file://0001-Add-the-plane-restrictionfor-SKL.-Otherwise-there-is.patch \
                    file://0002-Add-the-change-for-gvt-g-on-SKL.patch"
                    
KERNEL_EXTRA_FEATURES += " cfg/hv-guest.scc  cfg/paravirt_kvm.scc "
