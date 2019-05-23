接入文档备忘：
1、本功能组建库提供了两种动态权限的申请
 * 基于Activity、AppCompatActivity、Fragment基础上的动态权限申请，引入注解，简单接入
        具体需要注意的：
            需要申请的Activity需要实现两个接口IYZTPermissionCallback、IYZTRationaleCallbacks；在需要请求动态权限的方法上添加注解
            @OnPermissionGranted（...）
            代码如下：
        @OnPermissionGranted(PermissionAssembles.CODE_PHONES)
        private void callPhone() {
            if (YZTPermissions.hasPermissions(this,PermissionAssembles.PHONE)){
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"18621571083"));
                this.startActivity(intent);
            }else{
                YZTPermissions.requestPermission(new PermissionRequest.Builder(
                        this,PermissionAssembles.CODE_PHONES,PermissionAssembles.PHONE
                ).setRationale("该功能需要开启电话权限！").builder());
            }
        }

 * 不完全依赖与Activity、Fragment，在Service或者内部类等特殊地方都可使用，而且只需三个注解，简单操作
        具体需要注意的：
                @Permission(value = {Manifest.permission....},requestCode = ..) //请求的注解
                @PermissionDenied(requestCode = ..) //拒绝的注解
                @PermissionCanceled(requestCode = ..)  //拒绝且不再提醒的注解

                在Activity、AppCompatActivity、Fragment、Service中不需在参数中申请上下文，但在内部类中需要申请上下文及当前上下文的class
            代码如下：
        * 常用的处理方式：（需注意，当@Permission注解下的方法含有参数，@PermissionDenied、@PermissionCanceled注解下的方法也应该有相同的参数）
            @Permission(value = {Manifest.permission.SEND_SMS},requestCode = 200)
                private void requetsSMS() {
                    Toast.makeText(this, "请求权限_短信", Toast.LENGTH_SHORT).show();
                }
                @PermissionDenied(requestCode = 200)
                private void denySMS() {
                    Toast.makeText(this, "请求权限_短信_deny", Toast.LENGTH_SHORT).show();

                }
                @PermissionCanceled(requestCode = 200)
                private void cancleSMS() {
                    Toast.makeText(this, "请求权限_短信_cancle", Toast.LENGTH_SHORT).show();

                }

        * 特殊的处理方式：（1、注意同上 2、强制参数中含有Context（上下文）、当前类的Class ）
                @Permission(value = {Manifest.permission.CAMERA},requestCode = 100)
                private void openCamera(Context context,Class clz) {
                    Toast.makeText(context,"打开相机",Toast.LENGTH_LONG).show();
                }
                @PermissionCanceled(requestCode = 100)
                private void cancleOpenCamera(Context context,Class clz) {
                    Toast.makeText(context,"拒绝打开相机",Toast.LENGTH_LONG).show();
                }
                @PermissionDenied(requestCode = 100)
                private void deniedOpenCamera(Context context,Class clz) {
                    Toast.makeText(context,"拒绝且不再提醒打开相机",Toast.LENGTH_LONG).show();
                }
