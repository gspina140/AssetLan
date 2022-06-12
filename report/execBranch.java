case AVMLexer.BRANCHEQ:
                code[i++] = AVMParser.BRANCHEQ;
                labelRef.put(i++, (ctx.l != null ? ctx.l.getText() : null));
                code[i++] = registers.get(ctx.r1.getText());
                code[i++] = registers.get(ctx.r2.getText());
                break;
