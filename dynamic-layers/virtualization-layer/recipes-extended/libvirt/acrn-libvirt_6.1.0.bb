DESCRIPTION = "A toolkit to interact with the virtualization capabilities of recent versions of Linux." 
HOMEPAGE = "http://libvirt.org"
LICENSE = "LGPLv2.1+ & GPLv2+"
LICENSE:${PN}-ptest = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LESSER;md5=4b54a1fd55a448865a0b32d41598759d"
SECTION = "console/tools"

DEPENDS = "bridge-utils gnutls libxml2 lvm2 avahi parted curl libpcap util-linux e2fsprogs pm-utils \
	   iptables dnsmasq readline libtasn1 libxslt-native acl libdevmapper libtirpc \
           python3-docutils-native \
	   ${@bb.utils.contains('PACKAGECONFIG', 'polkit', 'shadow-native', '', d)} \
	   ${@bb.utils.contains('PACKAGECONFIG', 'gnutls', 'gnutls-native', '', d)}"

# libvirt-guests.sh needs gettext.sh
#
RDEPENDS:${PN} = "gettext-runtime"

RDEPENDS:${PN}-ptest += "make gawk perl bash"

RDEPENDS:${PN}-libvirtd += "bridge-utils iptables pm-utils dnsmasq netcat-openbsd"
RDEPENDS:${PN}-libvirtd:append:x86-64 = " dmidecode"
RDEPENDS:${PN}-libvirtd:append:x86 = " dmidecode"

CVE_PRODUCT = "libvirt"

#connman blocks the 53 port and libvirtd can't start its DNS service
RCONFLICTS:${PN}_libvirtd = "connman"

SRC_URI = "git://github.com/projectacrn/acrn-libvirt.git;branch=${SRCBRANCH};destsuffix=acrn-libvirt-${PV};name=libvirt \
           git://gitlab.com/keycodemap/keycodemapdb.git;protocol=https;destsuffix=acrn-libvirt-${PV}/src/keycodemapdb;name=keycodemapdb \
           file://tools-add-libvirt-net-rpc-to-virt-host-validate-when.patch \
           file://libvirtd.sh \
           file://libvirtd.conf \
           file://dnsmasq.conf \
           file://runptest.patch \
           file://run-ptest \
           file://libvirt-use-pkg-config-to-locate-libcap.patch \
           file://install-missing-file.patch \
           file://0001-ptest-Remove-Windows-1252-check-from-esxutilstest.patch \
           file://configure.ac-search-for-rpc-rpc.h-in-the-sysroot.patch \
           file://0001-build-drop-unnecessary-libgnu.la-reference.patch \
           file://gnutls-helper.py \
          "

SRCBRANCH = "dev-acrn-v6.1.0"
# with acrn v6.1.0
SRCREV_libvirt = "76cc3a01580349cfb5d2eaf7159d959990070313"
SRCREV_keycodemapdb = "27acf0ef828bf719b2053ba398b195829413dbdd"

inherit autotools gettext update-rc.d pkgconfig ptest systemd useradd perlnative
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "-r qemu; -r kvm"
USERADD_PARAM:${PN} = "-r -g qemu -G kvm qemu"

# Override the default set in autotools.bbclass so that we will use relative pathnames
# to our local m4 files.  This prevents an "Argument list too long" error during configuration
# if our project is in a directory with an absolute pathname of more than about 125 characters.
#
acpaths = "-I ./m4"

CACHED_CONFIGUREVARS += "\
ac_cv_path_XMLCATLOG=/usr/bin/xmlcatalog \
ac_cv_path_AUGPARSE=/usr/bin/augparse \
ac_cv_path_DNSMASQ=/usr/bin/dnsmasq \
ac_cv_path_BRCTL=/usr/sbin/brctl \
ac_cv_path_TC=/sbin/tc \
ac_cv_path_UDEVADM=/sbin/udevadm \
ac_cv_path_MODPROBE=/sbin/modprobe \
ac_cv_path_IP_PATH=/bin/ip \
ac_cv_path_IPTABLES_PATH=/usr/sbin/iptables \
ac_cv_path_IP6TABLES_PATH=/usr/sbin/ip6tables \
ac_cv_path_MOUNT=/bin/mount \
ac_cv_path_UMOUNT=/bin/umount \
ac_cv_path_MKFS=/usr/sbin/mkfs \
ac_cv_path_SHOWMOUNT=/usr/sbin/showmount \
ac_cv_path_PVCREATE=/usr/sbin/pvcreate \
ac_cv_path_VGCREATE=/usr/sbin/vgcreate \
ac_cv_path_LVCREATE=/usr/sbin/lvcreate \
ac_cv_path_PVREMOVE=/usr/sbin/pvremove \
ac_cv_path_VGREMOVE=/usr/sbin/vgremove \
ac_cv_path_LVREMOVE=/usr/sbin/lvremove \
ac_cv_path_LVCHANGE=/usr/sbin/lvchange \
ac_cv_path_VGCHANGE=/usr/sbin/vgchange \
ac_cv_path_VGSCAN=/usr/sbin/vgscan \
ac_cv_path_PVS=/usr/sbin/pvs \
ac_cv_path_VGS=/usr/sbin/vgs \
ac_cv_path_LVS=/usr/sbin/lvs \
ac_cv_path_PARTED=/usr/sbin/parted \
ac_cv_path_DMSETUP=/usr/sbin/dmsetup"

# Ensure that libvirt uses polkit rather than policykit, whether the host has
# pkcheck installed or not, and ensure the path is correct per our config.
CACHED_CONFIGUREVARS += "ac_cv_path_PKCHECK_PATH=${bindir}/pkcheck"

# Some other possible paths we are not yet setting
#ac_cv_path_RPCGEN=
#ac_cv_path_XSLTPROC=
#ac_cv_path_RADVD=
#ac_cv_path_UDEVSETTLE=
#ac_cv_path_EBTABLES_PATH=
#ac_cv_path_PKG_CONFIG=
#ac_cv_path_ac_pt_PKG_CONFIG
#ac_cv_path_POLKIT_AUTH=
#ac_cv_path_DTRACE=
#ac_cv_path_ISCSIADM=
#ac_cv_path_MSGFMT=
#ac_cv_path_GMSGFMT=
#ac_cv_path_XGETTEXT=
#ac_cv_path_MSGMERGE=
#ac_cv_path_SCRUB=
#ac_cv_path_PYTHON=

ALLOW_EMPTY:${PN} = "1"

PACKAGES =+ "${PN}-libvirtd ${PN}-virsh"

ALLOW_EMPTY:${PN}-libvirtd = "1"

FILES:${PN}-libvirtd = " \
	${sysconfdir}/init.d \
	${sysconfdir}/sysctl.d \
	${sysconfdir}/logrotate.d \
	${sysconfdir}/libvirt/libvirtd.conf \
        /usr/lib/sysctl.d/60-libvirtd.conf \
	${sbindir}/libvirtd \
	${systemd_unitdir}/system/* \
	${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', '', '${libexecdir}/libvirt-guests.sh', d)} \
	${@bb.utils.contains('PACKAGECONFIG', 'gnutls', '${sysconfdir}/pki/libvirt/* ${sysconfdir}/pki/CA/*', '', d)} \
        "

FILES:${PN}-virsh = " \
    ${bindir}/virsh \
    ${datadir}/bash-completion/completions/virsh \
"

FILES:${PN} += "${libdir}/libvirt/connection-driver \
	    ${datadir}/augeas \
	    ${@bb.utils.contains('PACKAGECONFIG', 'polkit', '${datadir}/polkit-1', '', d)} \
	    ${datadir}/bash-completion/completions/vsh \
	    ${datadir}/bash-completion/completions/virt-admin \
	    ${datadir}/libvirt/api \
	    ${datadir}/libvirt/cpu_map \
	    ${datadir}/libvirt/schemas \
	    ${datadir}/libvirt/test-screenshot.png \
	    /usr/lib/firewalld/zones/libvirt.xml \
	    "

FILES:${PN}-dbg += "${libdir}/libvirt/connection-driver/.debug ${libdir}/libvirt/lock-driver/.debug"
FILES:${PN}-staticdev += "${libdir}/*.a ${libdir}/libvirt/connection-driver/*.a ${libdir}/libvirt/lock-driver/*.a"

CONFFILES:${PN} += "${sysconfdir}/libvirt/libvirt.conf \
                    ${sysconfdir}/libvirt/lxc.conf \
                    ${sysconfdir}/libvirt/qemu-lockd.conf \
                    ${sysconfdir}/libvirt/qemu.conf \
                    ${sysconfdir}/libvirt/virt-login-shell.conf \
                    ${sysconfdir}/libvirt/virtlockd.conf"

CONFFILES:${PN}-libvirtd = "${sysconfdir}/logrotate.d/libvirt ${sysconfdir}/logrotate.d/libvirt.lxc \
                            ${sysconfdir}/logrotate.d/libvirt.qemu ${sysconfdir}/logrotate.d/libvirt.uml \
                            ${sysconfdir}/libvirt/libvirtd.conf \
                            /usr/lib/sysctl.d/libvirtd.conf"

INITSCRIPT_PACKAGES = "${PN}-libvirtd"
INITSCRIPT_NAME:${PN}-libvirtd = "libvirtd"
INITSCRIPT_PARAMS:${PN}-libvirtd = "defaults 72"

SYSTEMD_PACKAGES = "${PN}-libvirtd"
SYSTEMD_SERVICE:${PN}-libvirtd = " \
    libvirtd.service \
    virtlockd.service \
    libvirt-guests.service \
    virtlockd.socket \
    "


PRIVATE_LIBS:${PN}-ptest = " \
	libvirt-lxc.so.0 \
	libvirt.so.0 \
	libvirt-qemu.so.0 \
	lockd.so \
	libvirt_driver_secret.so \
	libvirt_driver_nodedev.so \
	libvirt_driver_vbox.so \
	libvirt_driver_interface.so \
	libvirt_driver_uml.so \
	libvirt_driver_network.so \
	libvirt_driver_nwfilter.so \
	libvirt_driver_qemu.so \
	libvirt_driver_storage.so \
	libvirt_driver_lxc.so \
    "

# xen-minimal config
#PACKAGECONFIG ??= "xen libxl xen-inotify test remote libvirtd"

# full config
PACKAGECONFIG ??= "qemu yajl openvz vmware vbox esx iproute2 lxc test \
                   remote macvtap libvirtd netcf udev python ebtables \
                   fuse iproute2 firewalld libpcap acrn \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'selinux audit libcap-ng', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'xen', 'libxl', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'polkit', 'polkit', '', d)} \
                  "
# acrn config recommended from ACRN Team
PACKAGECONFIG = "acrn remote macvtap libvirtd netcf udev ebtables fuse iproute2 firewalld libpcap test python storage-dir storage-fs storage-lvm "

# qemu is NOT compatible with mips64
PACKAGECONFIG:remove:mipsarchn32 = "qemu"
PACKAGECONFIG:remove:mipsarchn64 = "qemu"

# numactl is NOT compatible with arm
PACKAGECONFIG:remove:arm = "numactl"
PACKAGECONFIG:remove:armeb = "numactl"

# enable,disable,depends,rdepends
#
PACKAGECONFIG[gnutls] = ",,,gnutls-bin"
PACKAGECONFIG[qemu] = "--with-qemu --with-qemu-user=qemu --with-qemu-group=qemu,--without-qemu,qemu,"
PACKAGECONFIG[yajl] = "--with-yajl,--without-yajl,yajl,yajl"
PACKAGECONFIG[libxl] = "--with-libxl=${STAGING_DIR_TARGET}/lib,--without-libxl,xen,"
PACKAGECONFIG[openvz] = "--with-openvz,--without-openvz,,"
PACKAGECONFIG[vmware] = "--with-vmware,--without-vmware,,"
PACKAGECONFIG[vbox] = "--with-vbox,--without-vbox,,"
PACKAGECONFIG[esx] = "--with-esx,--without-esx,,"
PACKAGECONFIG[hyperv] = "--with-hyperv,--without-hyperv,,"
PACKAGECONFIG[polkit] = "--with-polkit,--without-polkit,polkit,polkit"
PACKAGECONFIG[lxc] = "--with-lxc,--without-lxc, lxc,"
PACKAGECONFIG[test] = "--with-test=yes,--with-test=no,,"
PACKAGECONFIG[remote] = "--with-remote,--without-remote,,"
PACKAGECONFIG[macvtap] = "--with-macvtap=yes,--with-macvtap=no,libnl,libnl"
PACKAGECONFIG[libvirtd] = "--with-libvirtd,--without-libvirtd,,"
PACKAGECONFIG[netcf] = "--with-netcf,--without-netcf,netcf,netcf"
PACKAGECONFIG[dtrace] = "--with-dtrace,--without-dtrace,,"
PACKAGECONFIG[udev] = "--with-udev --with-pciaccess,--without-udev,udev libpciaccess,"
PACKAGECONFIG[selinux] = "--with-selinux,--without-selinux,libselinux,"
PACKAGECONFIG[ebtables] = "ac_cv_path_EBTABLES_PATH=/sbin/ebtables,ac_cv_path_EBTABLES_PATH=,ebtables,ebtables"
PACKAGECONFIG[python] = ",,python3,"
PACKAGECONFIG[sasl] = "--with-sasl,--without-sasl,cyrus-sasl,cyrus-sasl"
PACKAGECONFIG[iproute2] = "ac_cv_path_IP_PATH=/sbin/ip,ac_cv_path_IP_PATH=,iproute2,iproute2"
PACKAGECONFIG[numactl] = "--with-numactl,--without-numactl,numactl,"
PACKAGECONFIG[fuse] = "--with-fuse,--without-fuse,fuse,"
PACKAGECONFIG[audit] = "--with-audit,--without-audit,audit,"
PACKAGECONFIG[libcap-ng] = "--with-capng,--without-capng,libcap-ng,"
PACKAGECONFIG[wireshark] = "--with-wireshark-dissector,--without-wireshark-dissector,wireshark libwsutil,"
PACKAGECONFIG[apparmor-profiles] = "--with-apparmor-profiles, --without-apparmor-profiles,"
PACKAGECONFIG[firewalld] = "--with-firewalld, --without-firewalld,"
PACKAGECONFIG[libpcap] = "--with-libpcap, --without-libpcap,libpcap,libpcap"
PACKAGECONFIG[numad] = "--with-numad, --without-numad,"
PACKAGECONFIG[acrn] = "--with-acrn,--without-acrn,,acrn-devicemodel acrn-tools"
PACKAGECONFIG[storage-dir] = "--with-storage-dir,--without-storage-dir,,"
PACKAGECONFIG[storage-fs] = "--with-storage-fs, --without-storage-fs,,"
PACKAGECONFIG[storage-lvm] = "--with-storage-lvm, --without-storage-lvm,,"


# Enable the Python tool support
require libvirt-python.inc

EXTRA_OECONF:append = " --disable-werror"
do_configure:prepend () {
    olddir=`pwd`
    cd ${S}
    autoreconf --verbose --force --install
    cd $olddir
}


do_compile() {
	cd ${B}/src
	# There may be race condition, but without creating these directories
	# in the source tree, generation of files fails.
	for i in access admin logging esx locking rpc hyperv lxc \
		    remote network storage interface nwfilter node_device \
		    secret vbox qemu acrn; do
		mkdir -p $i;
	done

	cd ${B}
	export PKG_CONFIG_PATH="$PKG_CONFIG_PATH:${B}/src:"
	oe_runmake all
}

do_install:prepend() {
	# so the install routines can find the libvirt.pc in the source dir
	export PKG_CONFIG_PATH="$PKG_CONFIG_PATH:${B}/src:"
}

do_install:append() {
	install -d ${D}/etc/init.d
	install -d ${D}/etc/libvirt
	install -d ${D}/etc/dnsmasq.d

	install -m 0755 ${WORKDIR}/libvirtd.sh ${D}/etc/init.d/libvirtd
	install -m 0644 ${WORKDIR}/libvirtd.conf ${D}/etc/libvirt/libvirtd.conf

	if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
	    # This will wind up in the libvirtd package, but will NOT be invoked by default.
	    #
	    mv ${D}/${libexecdir}/libvirt-guests.sh ${D}/${sysconfdir}/init.d
	fi

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
	    # This variable is used by libvirtd.service to start libvirtd in the right mode
	    sed -i '/#LIBVIRTD_ARGS="--listen"/a LIBVIRTD_ARGS="--listen --daemon"' ${D}/${sysconfdir}/sysconfig/libvirtd

	    # We can't use 'notify' when we don't support 'sd_notify' dbus capabilities.
	    sed -i -e 's/Type=notify/Type=forking/' \
	           -e '/Type=forking/a PIDFile=${localstatedir}/run/libvirtd.pid' \
		   ${D}/${systemd_unitdir}/system/libvirtd.service
	fi

	# The /var/run/libvirt directories created by the Makefile
	# are wiped out in volatile, we need to create these at boot.
	rm -rf ${D}${localstatedir}/run
	install -d ${D}${sysconfdir}/default/volatiles
	echo "d root root 0755 ${localstatedir}/run/libvirt none" \
	     > ${D}${sysconfdir}/default/volatiles/99_libvirt
	echo "d root root 0755 ${localstatedir}/run/libvirt/lockd none" \
	     >> ${D}${sysconfdir}/default/volatiles/99_libvirt
	echo "d root root 0755 ${localstatedir}/run/libvirt/lxc none" \
	     >> ${D}${sysconfdir}/default/volatiles/99_libvirt
	echo "d root root 0755 ${localstatedir}/run/libvirt/network none" \
	     >> ${D}${sysconfdir}/default/volatiles/99_libvirt
	echo "d root root 0755 ${localstatedir}/run/libvirt/qemu none" \
	     >> ${D}${sysconfdir}/default/volatiles/99_libvirt

	# Manually set permissions and ownership to match polkit recipe
	if ${@bb.utils.contains('PACKAGECONFIG', 'polkit', 'true', 'false', d)}; then
		install -d -m 0700 ${D}/${datadir}/polkit-1/rules.d
		chown polkitd ${D}/${datadir}/polkit-1/rules.d
		chgrp root ${D}/${datadir}/polkit-1/rules.d
	else
		rm -rf ${D}/${datadir}/polkit-1
	fi

	# disable seccomp_sandbox
        if [ -e ${D}${sysconfdir}/libvirt/qemu.conf ] ; then
	   sed -i '/^#seccomp_sandbox = 1/aseccomp_sandbox = 0' \
	       ${D}${sysconfdir}/libvirt/qemu.conf
        fi

	# Force the main dnsmasq instance to bind only to specified interfaces and
	# to not bind to virbr0. Libvirt will run its own instance on this interface.
	install -m 644 ${WORKDIR}/dnsmasq.conf ${D}/${sysconfdir}/dnsmasq.d/libvirt-daemon

	# remove .la references to our working diretory
	for i in `find ${D}${libdir} -type f -name *.la`; do
	    sed -i -e 's#-L${B}/src/.libs##g' $i
	done

	sed -i -e 's/^\(unix_sock_group\ =\ \).*/\1"kvm"/' ${D}/etc/libvirt/libvirtd.conf
	sed -i -e 's/^\(unix_sock_rw_perms\ =\ \).*/\1"0776"/' ${D}/etc/libvirt/libvirtd.conf

	case ${MACHINE_ARCH} in
		*mips*)
			break
			;;
		*)
			if ${@bb.utils.contains('PACKAGECONFIG','qemu','true','false',d)}; then
			    chown -R qemu:qemu ${D}/${localstatedir}/lib/libvirt/qemu
			    echo "d qemu qemu 0755 ${localstatedir}/cache/libvirt/qemu none" \
			        >> ${D}${sysconfdir}/default/volatiles/99_libvirt
			fi
			break
			;;
	esac

	if ${@bb.utils.contains('PACKAGECONFIG','gnutls','true','false',d)}; then
	    # Generate sample keys and certificates.
	    cd ${WORKDIR}
	    ${WORKDIR}/gnutls-helper.py -y

	    # Deploy all sample keys and certificates of CA, server and client
	    # to target so that libvirtd is able to boot successfully and local
	    # connection via 127.0.0.1 is available out of box.
	    install -d ${D}/etc/pki/CA
	    install -d ${D}/etc/pki/libvirt/private
	    install -m 0755 ${WORKDIR}/gnutls-helper.py ${D}/${bindir}
	    install -m 0644 ${WORKDIR}/cakey.pem ${D}/${sysconfdir}/pki/libvirt/private/cakey.pem
	    install -m 0644 ${WORKDIR}/cacert.pem ${D}/${sysconfdir}/pki/CA/cacert.pem
	    install -m 0644 ${WORKDIR}/serverkey.pem ${D}/${sysconfdir}/pki/libvirt/private/serverkey.pem
	    install -m 0644 ${WORKDIR}/servercert.pem ${D}/${sysconfdir}/pki/libvirt/servercert.pem
	    install -m 0644 ${WORKDIR}/clientkey.pem ${D}/${sysconfdir}/pki/libvirt/private/clientkey.pem
	    install -m 0644 ${WORKDIR}/clientcert.pem ${D}/${sysconfdir}/pki/libvirt/clientcert.pem

	    # Force the connection to be tls.
	    sed -i -e 's/^\(listen_tls\ =\ .*\)/#\1/' -e 's/^\(listen_tcp\ =\ .*\)/#\1/' ${D}/etc/libvirt/libvirtd.conf
	fi

	# virt-login-shell needs to run with setuid permission
	chmod 4755 ${D}${bindir}/virt-login-shell
}

EXTRA_OECONF += " \
    --with-init-script=systemd \
    --with-test-suite \
    "

# gcc9 end up mis-compiling qemuxml2argvtest.o with Og which then
# crashes on target, so remove -Og and use -O2 as workaround
SELECTED_OPTIMIZATION:remove:virtclass-multilib-lib32:mipsarch = "-Og"
SELECTED_OPTIMIZATION:append:virtclass-multilib-lib32:mipsarch = " -O2"

EXTRA_OEMAKE = "BUILD_DIR=${B} DEST_DIR=${D}${PTEST_PATH} PTEST_DIR=${PTEST_PATH} SYSTEMD_UNIT_DIR=${systemd_system_unitdir}"

PRIVATE_LIBS:${PN}-ptest:append = "libvirt-admin.so.0"

do_compile_ptest() {
	oe_runmake -C tests buildtest-TESTS
}

do_install_ptest() {
	oe_runmake -C tests install-ptest

	find ${S}/tests -maxdepth 1 -type d -exec cp -r {} ${D}${PTEST_PATH}/tests/ \;

	# remove .la files for ptest, they aren't required and can trigger QA errors
	for i in `find ${D}${PTEST_PATH} -type f \( -name *.la -o -name *.o \)`; do
                rm -f $i
	done
}

pkg_postinst:${PN}() {
        if [ -z "$D" ] && [ -e /etc/init.d/populate-volatile.sh ] ; then
                /etc/init.d/populate-volatile.sh update
        fi
        mkdir -m 711 -p $D/data/images
}

python () {
    if not bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d):
        d.setVar("INHIBIT_UPDATERCD_BBCLASS", "1")
}

PROVIDES_${PN} = "libvirt"
RPROVIDES:${PN} += " libvirt"

PROVIDES_${PN}-libvirtd = "libvirt-libvirtd"
RPROVIDES:${PN}-libvirtd += " libvirt-libvirtd"

PROVIDES_${PN}-virsh = "libvirt-virsh"
RPROVIDES:${PN}-virsh += " libvirt-virsh"

PROVIDES_${PN}-python = "libvirt-python"
RPROVIDES:${PN}-python += " libvirt-python"

RPROVIDES:${PN}-ptest += " libvirt-ptest "

CVE_CHECK_WHITELIST += "CVE-2014-8135 CVE-2014-8136 CVE-2015-5313 CVE-2017-1000256 CVE-2018-5748 CVE-2018-6764 CVE-2019-3886"

#CVE-2014-8135:
#Fixed in v1.2.11
#Fixed by: https://libvirt.org/git/?p=libvirt.git;a=commit;h=87b9437f8951f9d24f9a85c6bbfff0e54df8c984
#Ref: https://security.libvirt.org/2014/0009.html

#CVE-2014-8136:
#Fixed in v1.2.11
#Fixed by: https://libvirt.org/git/?p=libvirt.git;a=commit;h=2bdcd29c713dfedd813c89f56ae98f6f3898313d
#Ref: https://security.libvirt.org/2014/0010.html

#CVE-2015-5313:
#Fixed in v1.3.1
#Fixed by: https://libvirt.org/git/?p=libvirt.git;a=commit;h=034e47c338b13a95cf02106a3af912c1c5f818d7
#Ref: https://security.libvirt.org/2015/0004.html

#CVE-2017-1000256
#Fixed in v3.9.0
#Fixed by: https://libvirt.org/git/?p=libvirt.git;a=commit;h=441d3eb6d1be940a67ce45a286602a967601b157
#Ref: https://security.libvirt.org/2017/0002.html

#CVE-2018-5748:
#Fixed in v4.0.0
#Fixed by: https://libvirt.org/git/?p=libvirt.git;a=commit;h=bc251ea91bcfddd2622fce6bce701a438b2e7276
#Ref: https://security.libvirt.org/2018/0002.html

#CVE-2018-6764:
#Fixed in v4.1.0
#Fixed by: https://libvirt.org/git/?p=libvirt.git;a=commit;h=c2dc6698c88fb591639e542c8ecb0076c54f3dfb
#Ref: https://security.libvirt.org/2018/0003.html

#CVE-2019-3886:
#Fixed in v5.3.0
#Fixed by: https://libvirt.org/git/?p=libvirt.git;a=commit;h=ae076bb40e0e150aef41361b64001138d04d6c60
#and
#https://libvirt.org/git/?p=libvirt.git;a=commit;h=2a07c990bd9143d7a0fe8d1b6b7c763c52185240
#Ref: https://security.libvirt.org/2019/0001.html
