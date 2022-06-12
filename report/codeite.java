public String codeGeneration(){
        String falseL = AssetLanlib.freshLabel();
        String end = AssetLanlib.freshLabel();

        String thenCode = "";
        String elseCode = "";

        for(Node t : thenStsL)
            thenCode+=t.codeGeneration()+"\n";

        for(Node e : elseStsL)
            elseCode+=e.codeGeneration()+"\n";

        return cond.codeGeneration()+
                "li $t1 0\n"+
                "beq $a0 $t1 "+falseL+"\n"+
                thenCode+
                "b "+end+"\n"+
                falseL+":\n"+
                elseCode+
                end+":\n";
    } 
