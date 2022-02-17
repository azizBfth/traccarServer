package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;
import org.traccar.model.Position;

public class T800xProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        T800xProtocolDecoder decoder = new T800xProtocolDecoder(null);

        verifyAttribute(decoder, binary(
                "2727020049052e086528404072393849002008060310110000000068b7c8c286eaa441000000008000008100001617410700019ce782b0001e000002581e00000530d4801f00000000"),
                Position.KEY_BATTERY_LEVEL, 100);

        verifyPosition(decoder, binary(
                "262602005308090865284040309670000f000f0f0000005a47c000050100000020000000008bfd0020022505185300004041dcc9d6c243b3c6410000012712400000000009e2ffffffffffffffffffffffff09"));

        verifyPosition(decoder, binary(
                "2727040049001b0866425039645728c916190604005240000000007739d2c25b681f420000000080000081000020174105000005458216001e000000f01e00001e30d0000000000000"));

        verifyAttribute(decoder, binary(
                "272705005e000108664250328807851905301107481054002d004d006f00620069006c006500074341542d4e42310a4c54452042414e4420340f333130323430323030303032333030143839303132343032303531303030323330303746"),
                Position.KEY_OPERATOR, "T-Mobile");

        verifyPosition(decoder, binary(
                "272702004904a90866425032880785c800190530080350000000000705eec29bf50842000000000008008090502a003700000a9e358002003c000003841900001e3f90000000000000272702004904aa0866425032880785c800190530081851000000000705eec29bf50842000000000008008090602e003700000a9e358002003c000003841900001e3f90000000000000"));

        verifyNull(decoder, binary(
                "2727010017000108806168988888881016010207110111"));

        verifyNull(decoder, binary(
                "252501001504050880061689888888111111250350"));

        verifyAttribute(decoder, binary(
                "2525810128000108664250328959160149004d00450049003a003800360036003400320035003000330032003800390035003900310036002c005300450054002000560045005200530049004f004e0020004f004b002c00560065007200730069006f006e003a00420061007300690063003a00560031002e0030002e0030002c004100500050003a00560034002e0032002e0033002c004200550049004c0044003a0032003000310039002d00300033002d00330030002c00300038003a00300035002c0050004c0054003a0032003500300033004100560045002c00480057003a00560032002e0031002c004d004f00440045004c003a002c004d004f00440045004d003a0042003900470036004d0041005200300032004100300037004d00310047002300"),
                Position.KEY_RESULT, "IMEI:866425032895916,SET VERSION OK,Version:Basic:V1.0.0,APP:V4.2.3,BUILD:2019-03-30,08:05,PLT:2503AVE,HW:V2.1,MODEL:,MODEM:B9G6MAR02A07M1G#");

        verifyPosition(decoder, binary(
                "2525020044a66d0862522030401350001403841409c40064edc000051100960000071701370000003ea7ee0019032010581300000000aad3e1bda6f24d42000000001281"));

        verifyPosition(decoder, binary(
                "252502004400010880616898888888000A00FF2001000020409600989910101010055501550000101005050005051010050558866B4276D6E342912AB441111500051010"));

        verifyNull(decoder, binary(
                "232301001500000880316890202968140197625020"));

        verifyNull(decoder, binary(
                "232303000f00000880316890202968"));

        verifyAttributes(decoder, binary(
                "232302004200000880316890202968001e02582d00000000000000050000320000018901920000001dc1e2001601081154255d0202005a0053875a00a57e5a00af80"));

        verifyNull(decoder, binary(
                "232301001500020357367031063979150208625010"));

        verifyNull(decoder, binary(
                "232303000f00000357367031063979"));

        verifyPosition(decoder, binary(
                "232304004200030357367031063979003c03842307d00000c80000050100008000008900890100000017b100151022121648b8ef0c4422969342cec5944100000110"));

        verifyPosition(decoder, binary(
                "232302004200150357367031063979003c03842307d000004a0000050100004001009500940000000285ab001510281350477f710d4452819342d1ba944101160038"));

        verifyAttributes(decoder, binary(
                "232302004200000357367031063979003c03842307d000008000000501000000010094009400000002a0b90015102814590694015a00620cf698620cf49e620cf498"));

    }

}
