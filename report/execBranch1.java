case AVMParser.BRANCHEQ: 
                        address = code[ip++];
                        v1 = registers[code[ip++]];
                        v2 = registers[code[ip++]];
                        if (v2 == v1)
                            ip = address;
                        break;
