import java.io.*;
import java.util.*;

public class prac{
    private String[][] symtab;
    private String[][] littab;
    private int[][] pooltab;
    private String[][] ic;
    private int lc;
    private int c;
    private int litc;
    private int pool_c;
    private int pool_i;
    private int ic_c;
    private int ic_l;
    private int pool_ctr;
    private static int i4=0;
    

    prac()
    {
        symtab=new String[20][2];
        ic=new String[30][3];
        littab=new String[20][3];
        for(int i=0;i<20;i++)
        {
            littab[i][2]="0";
        }
        pooltab=new int[10][2];
        for(int i=0;i<10;i++)
        {
            pooltab[i][0]=i;
            pooltab[i][1]=0;
        }
        lc=0;
        c=0;
        litc=0;
        pool_c=1;
        pool_i=1;
        ic_c=0;
        ic_l=0;
        pool_ctr=0;
        
    }

    public void pass1(String inputfile, String outputfile)throws IOException{
        symtab(inputfile);
        create_ic(inputfile);
        print_symtab();
        print_littab();
        print_pooltab();
       print_ic(); 
        write(outputfile);   
    }

    private void write(String outputfile)throws IOException{
    try
    (FileWriter w =new FileWriter(outputfile)){
    String t="#symbol table:";
    w.write(t);
    w.write(System.lineSeparator());
    for (int i=0;i<c;i++)
    {
        w.write(symtab[i][0]+" "+symtab[i][1]);
        w.write(System.lineSeparator());
        }
    t="#literal table:";
    w.write(t);
    w.write(System.lineSeparator());
    for (int i=0;i<litc;i++)
    {
        w.write(littab[i][0]+" "+littab[i][1]+" "+littab[i][2]);
        w.write(System.lineSeparator());
        }
    t="#pool table:";
    w.write(t);
    w.write(System.lineSeparator());
    for (int i=0;i<pool_i;i++)
    {
        w.write(pooltab[i][0]+" "+pooltab[i][1]);
        w.write(System.lineSeparator());
        }
    t="#intermediate code:";
    w.write(t);
    w.write(System.lineSeparator());
    for (int i=0;i<ic_c;i++)
    {
        w.write(ic[i][0]+" "+ic[i][1]+" "+ic[i][2]);
        w.write(System.lineSeparator());
        }
        w.close();
        }}


    private void symtab(String inputfile)throws IOException{
        try
        (BufferedReader r=new BufferedReader(new FileReader(inputfile))){
        String line;
        while((line=r.readLine())!=null){
        String[] token=line.split("\\s+");
        scan_lit(token);
        
                
        if(token[0].equals("NULL"))   // labels declaration
        {
            if(token[1].equals("START"))   // if start then load lc 
            {
                lc=Integer.parseInt(token[2]);
                lc--;
            }
        
        else if(token[1].equals("LTORG"))
        {
            for(int i=0;i<litc;i++)
            {
                if(littab[i][2].equals("0"))
                {
                    littab[i][2]=String.valueOf(lc);
                    lc++;
                    pool_c=litc;
                }
            }
            pooltab[pool_i][0]=pool_i;
            pooltab[pool_i][1]=pool_c;
            pool_i++;
            lc--;
        }

        else if(token[1].equals("END"))
        {
            for(int i=0;i<litc;i++)
            {
                if(littab[i][2].equals("0"))
                {
                    littab[i][2]=String.valueOf(lc);
                    lc++;
                }
            }
        }
        else if(token[1].equals("ORIGIN"))
        {
            String[] word=token[2].split("\\+");
            for(int i=0;i<=c;i++)
            {
                if(word[0].equals(symtab[i][0]))
                {
                    int addr=Integer.parseInt(symtab[i][1]);
                    int a=Integer.parseInt(word[1]);
                    lc=addr+a-1;
                }
            }
}
        }        
        else
        {
            symtab[c][0]=token[0];
            symtab[c][1]=String.valueOf(lc);
            c++;
          
            if(token[1].equals("EQU")){
            for(int i=0;i<=c;i++)
            {
                if(token[2].equals(symtab[i][0]))
                {
                    symtab[c-1][1]=symtab[i][1];
                }
                
            }
            lc--;      
            }
           
        }
        
        lc++;
        }
        }
   }

   private void print_symtab()
   {
     System.out.print("symbol table\n");
    for(int i=0;i<c;i++)
    {
        System.out.print(symtab[i][0]+"\t"+symtab[i][1]+"\n");
    }
   }
   private void print_pooltab()
   {
    System.out.print("pool table\n");
    for(int i=0;i<c-1;i++)
    {
        System.out.print(pooltab[i][0]+"\t"+pooltab[i][1]+"\n");
    }
   }


   private void print_ic()
   {
    System.out.print("intermediate code\n");
    for(int i=0;i<ic_c;i++)
    {
        String t="";
        if(ic[i][0]!=null)
        {t+=ic[i][0];}
        if(ic[i][1]!=null)
        {t+=ic[i][1];}
        if(ic[i][2]!=null)
        {t+=ic[i][2];}
        System.out.print(t+"\n");
    }
   }

   private void print_littab()
   {
     System.out.print("literal table\n");
    for (int i=0;i<litc;i++)
    {
        System.out.println(littab[i][0]+"\t"+littab[i][1]+"\t"+littab[i][2]+"\n");
    }
   }

   private boolean isDigit(String s){
    try {
        Double.parseDouble(s);
        return true;
    }catch(NumberFormatException e1)
    {
        return false;
    }
   }

   private void scan_lit(String s[]){
    if(isDigit(s[3])) // here if the token[3] is numerical then it should be stored in littab
            {
            littab[litc][0]=String.valueOf(litc);
            littab[litc][1]=s[3];
            
            litc++;
            }
   }


   private void create_ic(String inputfile)throws IOException{
        try
        (BufferedReader r=new BufferedReader(new FileReader(inputfile))){
        String line;
        while((line=r.readLine())!=null){
        String[] s=line.split("\\s+");
    if (s[1].equals("LTORG")) {
            pool_ctr = pooltab[ic_l][1];

            while (pool_ctr < pooltab[ic_l+1][1]) {
                ic[ic_c][0] = "(DL,02)";
                ic[ic_c][1] = String.format("(C,%s)", littab[pool_ctr][1]);
                pool_ctr++;
                ic_c++;
            }
            //pooltab[ic_l + 1][1] = litc;
            ic_l++;
        } else if (s[1].equals("END")) {
            pool_ctr = pooltab[ic_l][1];

            ic[ic_c][0] = "(AD,02)";
            ic_c++;
            while (pool_ctr < litc) {
                ic[ic_c][0] = "(DL,02)";
                ic[ic_c][1] = String.format("(C,%s)", littab[pool_ctr][1]);
                pool_ctr++;
                ic_c++;
            }
    }

    else 
    {
        ic[ic_c][0]=token2(s[1]);
        ic[ic_c][1]=token3(s[2]);
        ic[ic_c][2]=token4(s[3]);
        ic_c++;
    }

   }}}

   private String token2(String m){
    String t=null;
    if(m.equals("START"))
    {t="(AD,01)";}
    else if(m.equals("ORIGIN"))
    {t="(AD,03)";}
    else if(m.equals("EQU"))
    {t= "(AD,04)";}
    else if(m.equals("STOP"))
    {t= "(IS,00)";}
    else if(m.equals("ADD"))
    {t= "(IS,01)";}
    else if(m.equals("SUB"))
    {t= "(IS,02)";}
    else if(m.equals("MULT"))
    {t= "(IS,03)";}
    else if(m.equals("MOVER"))
    {t= "(IS,04)";}
    else if(m.equals("MOVEM"))
    {t= "(IS,05)";}
    else if(m.equals("COMP"))
    {t= "(IS,06)";}
    else if(m.equals("BC"))
    {t= "(IS,07)";}
    else if(m.equals("DIV"))
    {t= "(IS,08)";}
    else if(m.equals("READ"))
    {t= "(IS,09)";}
    else if(m.equals("PRINT"))
    {t="(IS,10)";}
    else if(m.equals("DS"))
{t= "(DL,01)";}
else if(m.equals("DC"))
{t= "(DL,02)";}
    return t;
   }


    private String token4(String m)
    {String t = null;
    
        if(m.equals("NULL"))
        {}
        else{
    for (int j = 0; j < 5; j++) {
        if (m.equals(symtab[j][0])) {
            t = String.format("(S,%s)", j);
            break;
        }
    }
    if (t == null) {
            if (m.equals(littab[i4][1])) {
                t = String.format("(L,%s)", i4);
                
            }
        i4++;
    }
        }
    return t;}


    private String token3(String m)
    {
        String t=null;
        if(m.equals("AREG"))
    t="(RG,01)";
    else if(m.equals("BREG"))
    t="(RG,02)";
    else if(m.equals("CREG"))
    t="(RG,03)";
    else if(isDigit(m))
     {t=String.format("(C, %s)",m);}
    else if(m.equals("EQ"))
    t="(BC,01)";
    else if(m.equals("NE"))
    t="(BC,02)";
    else if(m.equals("LT"))
    t="(BC,03)";
    else if(m.equals("GT"))
    t="(BC,04)";
    else if(m.equals("LE"))
    t="(BC,05)";
    else if(m.equals("GE"))
    t="(BC,06)";
    else if(m.equals("ANY"))
    t="(BC,07)";
    else
    {
        int add=0;
        String ab[]=m.split("\\+");
        for(int i=0;i<5;i++)
        {if(ab[0].equals(symtab[i][0]))
        add=Integer.parseInt(symtab[i][1]);}
        if(ab.length==2){
        add+=Integer.parseInt(ab[1]);}
        t=String.format("(C,%s)",add);

        
    }
    
    return t;
    }
    

    public static void main(String[] args)
    {
        if (args.length!=2)
        {
            System.out.print("java pract <input file> <output file>");
            return;
        }
        prac assemble = new prac();
        try {
            assemble.pass1(args[0], args[1]);
            
        } catch (IOException e) {
            System.err.println("Error in assembler: " + e.getMessage());
        }
    }
}
