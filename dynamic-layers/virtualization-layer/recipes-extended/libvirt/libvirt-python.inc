inherit python3native python3-dir python3targetconfig

export STAGING_INCDIR
export STAGING_LIBDIR
export BUILD_SYS 
export HOST_SYS

RDEPENDS:${PN}-python += "python3"
PACKAGECONFIG_${PN}-python[xen] = ",,,xen-python"

PACKAGES += "${PN}-python-staticdev ${PN}-python-dev ${PN}-python-dbg ${PN}-python"

FILES:${PN}-python-staticdev += "${PYTHON_SITEPACKAGES_DIR}/*.a"
FILES:${PN}-python-dev += "${PYTHON_SITEPACKAGES_DIR}/*.la"
FILES:${PN}-python-dbg += "${PYTHON_SITEPACKAGES_DIR}/.debug/"
FILES:${PN}-python = "${bindir}/* ${libdir}/* ${libdir}/${PYTHON_DIR}/*"

SRC_URI += "http://libvirt.org/sources/python/libvirt-python-${PV}.tar.gz;name=libvirt_python"

SRC_URI[libvirt_python.md5sum] = "2834626b07da6ac4ca1559abfd55c118"
SRC_URI[libvirt_python.sha256sum] = "be644f4809c0e1d368e3ac065df3c66a26dcfe61ecb607ee9706e1799f22c35a"

export LIBVIRT_API_PATH = "${S}/docs/libvirt-api.xml"
export LIBVIRT_CFLAGS = "-I${S}/include"
export LIBVIRT_LIBS = "-L${B}/src/.libs -lvirt -ldl"
export LDFLAGS="-L${B}/src/.libs"

LIBVIRT_INSTALL_ARGS = "--root=${D} \
    --prefix=${prefix} \
    --install-lib=${PYTHON_SITEPACKAGES_DIR} \
    --install-data=${datadir}"

python __anonymous () {
    pkgconfig = d.getVar('PACKAGECONFIG')
    if ('python') in pkgconfig.split():
        d.setVar('LIBVIRT_PYTHON_ENABLE', '1')
    else:
        d.setVar('LIBVIRT_PYTHON_ENABLE', '0')
}

do_compile:append() {
	if [ "${LIBVIRT_PYTHON_ENABLE}" = "1" ]; then
		# we need the python bindings to look into our source dir, not
		# the syroot staged pkgconfig entries. So we clear the sysroot
		# for just this portion.
		export PKG_CONFIG_SYSROOT_DIR=
		cd ${WORKDIR}/libvirt-python-${PV} && \
		  ${STAGING_BINDIR_NATIVE}/python3-native/python3 setup.py build
	fi
}

do_install:append() {
	if [ "${LIBVIRT_PYTHON_ENABLE}" = "1" ]; then
		# we need the python bindings to look into our source dir, not
		# the syroot staged pkgconfig entries. So we clear the sysroot
		# for just this portion.
		export PKG_CONFIG_SYSROOT_DIR=
		cd ${WORKDIR}/libvirt-python-${PV} && \
		  ${STAGING_BINDIR_NATIVE}/python3-native/python3 setup.py install \
                       --install-lib=${D}/${PYTHON_SITEPACKAGES_DIR} ${LIBVIRT_INSTALL_ARGS}
	fi
}
