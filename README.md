# verytransfer
Verificador de transferencias de Saldo CUBACEL. Pasarela de pago sin conexión.
La biblioteca verifica si un determinado cliente ha realizado alguna transferencia de saldo a un número determinado, una cantidad determinada. Se puede especificar una cantidad de días de caducidad de este pago.

1-A nivel de proyecto:
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

2-A nivel de aplicacion:
implementation 'com.github.leandrotorressilva:verytransfer:1.0'

3-Uso:

        //sample usage
        VerifyAsyncTask verifyAsyncTask = new VerifyAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(TransferInfo output) {
                if (output != null) {
                    Toast.makeText(MainActivity.this, "VERIFICADO", Toast.LENGTH_LONG).show();
                    //pay found under requested params:
                    //3.0 CUP - cantidad a pagar
                    //54868618 - número que debe recibir el pago
                    //false - significa que el pago puede ser 3 o más, true - significa que debe ser exactamente 3
                    //30 - duración del pago
                } else {
                    Toast.makeText(MainActivity.this, "NO VERIFICADO", Toast.LENGTH_LONG).show();
                }
            }
        }, MainActivity.this, 3.0, "54868618", false, 30);
        verifyAsyncTask.execute();

