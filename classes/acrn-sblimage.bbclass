inherit python3native
DEPENDS += "slimboot-tools-native"

MB_DEPENDENCY    ?= ""
MB_MC_DEPENDENCY ?= ""
MB_ACRN_BINARY   ?= "${DEPLOY_DIR_IMAGE}/acrn.32.out"
MB_ACRN_CMDLINE  ?= ""
MB_ACRN_MODULES  ?= "${TOPDIR}/conf/linux.txt;${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}"
BASE_SBLIMAGE    ?= "sbl_os"
SBLIMAGE_NAME    ?= "${BASE_SBLIMAGE}"
PREGENERATED_SIGNING_KEY_SLIMBOOT_KEY_SHA256 ?= "${TOPDIR}/cert/TestSigningPrivateKey.pem"
BB_CURRENT_MC    ?= ""

python do_acrn_sblimage() {
    import re
    import os
    import subprocess

    mbAcrnBinaryDeployDir = d.getVar('MB_ACRN_BINARY').lstrip().rstrip()
    if not os.path.isfile(mbAcrnBinaryDeployDir):
        bb.fatal("acrn %s not found!" % mbAcrnBinaryDeployDir)

    mbAcrnCmdlineDeployDir = d.getVar('MB_ACRN_CMDLINE').lstrip().rstrip()
    if mbAcrnCmdlineDeployDir == "":
        hv_cmdline = d.getVar('WORKDIR') + "/hv_cmdline"
        bb.debug(1, "hv_cmdline: %s" % (hv_cmdline))
        subprocess.check_call("echo 'uart=mmio@0xfe042000' > %s" % (hv_cmdline), shell=True)
    else:
        hv_cmdline = mbAcrnCmdlineDeployDir

    mbAcrnBinaryDeployDirPairList = re.split(" +", d.getVar('MB_ACRN_MODULES').lstrip().rstrip())

    genContainerPath = "%s/%s/slimboot/Tools/GenContainer.py" % (d.getVar('STAGING_DIR_NATIVE'), d.getVar('libexecdir'))

    genContainerCmd = "%s" % d.getVar('PYTHON')
    genContainerCmd = genContainerCmd + " %s create" % (genContainerPath)
    genContainerCmd = genContainerCmd + " -cl"
    genContainerCmd = genContainerCmd + " CMDL:%s" % (hv_cmdline)
    genContainerCmd = genContainerCmd + " ACRN:%s" % (mbAcrnBinaryDeployDir)
    counter = 0
    for mbAcrnBinaryDeployDirPair in mbAcrnBinaryDeployDirPairList:
        mbAcrnBinaryDeployDirPairMods = re.split(";", mbAcrnBinaryDeployDirPair.strip())
        bb.debug(1, "module[%s]: %s" % (counter,   mbAcrnBinaryDeployDirPairMods[0]))
        bb.debug(1, "module[%s]: %s" % (counter+1, mbAcrnBinaryDeployDirPairMods[1]))

        if not os.path.isfile(mbAcrnBinaryDeployDirPairMods[0]):
            bb.fatal("module %s not found!" % mbAcrnBinaryDeployDirPairMods[0])

        if not os.path.isfile(mbAcrnBinaryDeployDirPairMods[1]):
            bb.fatal("module %s not found!" % mbAcrnBinaryDeployDirPairMods[1])

        genContainerCmd = genContainerCmd + " MOD%s:%s MOD%s:%s" % (counter, mbAcrnBinaryDeployDirPairMods[0], counter+1, mbAcrnBinaryDeployDirPairMods[1])
        counter = counter + 2
    genContainerCmd = genContainerCmd + " -o %s/%s" % (d.getVar('WORKDIR'), d.getVar('SBLIMAGE_NAME'))
    genContainerCmd = genContainerCmd + " -k %s" % (d.getVar('PREGENERATED_SIGNING_KEY_SLIMBOOT_KEY_SHA256'))
    genContainerCmd = genContainerCmd + " -t MULTIBOOT"

    bb.debug(1, "genContainerCmd: %s" % (genContainerCmd))
    subprocess.check_call(genContainerCmd, shell=True)
    subprocess.check_call("install -d %s/boot; install -m 644 %s/%s %s/boot" % (d.getVar('IMAGE_ROOTFS'), d.getVar('WORKDIR'), d.getVar('SBLIMAGE_NAME'), d.getVar('IMAGE_ROOTFS')), shell=True)
}

python() {
    import re

    currentMultibootConfig = d.getVar('BB_CURRENT_MC')
    multibootPackageDependencyList = re.split(" +", d.getVar('MB_DEPENDENCY').lstrip().rstrip())
    multibootPackageMcDependencyList = re.split(" +", d.getVar('MB_MC_DEPENDENCY').lstrip().rstrip())

    bb.debug(1, "Multiboot variable parsing check")

    for multibootPackageDependency in multibootPackageDependencyList:
        if multibootPackageDependency:
            dependency = "%s" % (multibootPackageDependency)
            bb.debug(1, "MultibootDependency: %s" % (dependency))
            d.appendVarFlag('do_acrn_sblimage', 'depends', ' ' + dependency)

    for multibootPackageMcDependency in multibootPackageMcDependencyList:
        if multibootPackageMcDependency:
            mcdependency = "multiconfig:%s:%s" % (currentMultibootConfig, multibootPackageMcDependency)
            bb.debug(1, "MultibootMcDependency: %s" % (mcdependency))
            d.appendVarFlag('do_acrn_sblimage', 'mcdepends', ' ' + mcdependency)

    # fix host-user-contaminated QA warnings
    d.setVarFlag('do_acrn_sblimage', 'fakeroot', '1')
    d.setVarFlag('do_acrn_sblimage', 'umask', '022')

}

addtask do_acrn_sblimage after do_rootfs before do_image
