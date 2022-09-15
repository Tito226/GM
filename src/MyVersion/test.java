package MyVersion;

import MyVersion.Core.Dot;
import MyVersion.Core.Dot_Type;
import MyVersion.Core.Network;

import java.util.ArrayList;
import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        ArrayList<Dot> dd=new ArrayList<Dot>();
        dd.add(new Dot(Dot_Type.HIDDEN));
        dd.add(new Dot(Dot_Type.HIDDEN));
        dd.add(new Dot(Dot_Type.HIDDEN));
        dd.add(new Dot(Dot_Type.HIDDEN));
        dd.add(new Dot(Dot_Type.HIDDEN));
        dd.add(new Dot(Dot_Type.HIDDEN));

        for(Dot d: dd){
            d.value=12;
        }
        for (int i = 0; i <dd.size() ; i++) {
            System.out.println(dd.get(i).value);
        }
    }
}
