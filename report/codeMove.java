public String codeGeneration(){
        String getAR1="";
        String getAR2="";
        
        for(int i = 0; i < nl-entry1.getNestinglevel(); i++)
            getAR1+="lw $al 0($al)\n";

        for(int i = 0; i < nl-entry2.getNestinglevel(); i++)
            getAR2+="lw $al 0($al)\n";

        return "move $al $fp\n"+
                getAR1+
                "lw $a0 "+entry1.getOffset()+"($al)\n"+
                "push $a0\n"+
                "li $t1 0\n"+
                "sw $t1 "+entry1.getOffset()+"($al)\n"+
                "move $al $fp\n"+
                getAR2+
                "lw $a0 "+entry2.getOffset()+"($al)\n"+
                "lw $t1 0($sp)\n"+  // top
                "add $a0 $a0 $t1\n"+
                "sw $a0 "+entry2.getOffset()+"($al)\n"+
                "pop\n";
    }
