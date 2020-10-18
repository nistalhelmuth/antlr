import java.util.*; 

public class ARMArquitecture {
  public Stack<Integer> freeParams = new Stack<Integer>();
  public Stack<Integer> freeRegisters = new Stack<Integer>();
  public Stack<Integer> usedRegisters = new Stack<Integer>();
  public Deque<Integer> usedLabels = new LinkedList<Integer>();
  public Integer registerCount = 0;


  public ARMArquitecture() {
    this.freeRegisters.push(10);
    this.freeRegisters.push(9);
    this.freeParams.push(8);

    this.freeParams.push(4);
    this.freeParams.push(3);
    this.freeParams.push(2);
    this.freeParams.push(1);
    this.freeParams.push(0);
  }

  public Integer getRegister(){
    Integer reg = freeRegisters.pop();
    usedRegisters.push(reg);
    return reg;
  }

  public Integer getLastRegister(){
    Integer reg = usedRegisters.pop();
    freeRegisters.push(reg);
    return reg;
  }

  public Integer getParam(){
    Integer reg = freeParams.pop();
    return reg;
  }

  public void freeParams(){
    this.freeParams = new Stack<Integer>();
    this.freeParams.push(4);
    this.freeParams.push(3);
    this.freeParams.push(2);
    this.freeParams.push(1);
    this.freeParams.push(0);
  }

  public Integer createLabel(){
    usedLabels.push(usedLabels.size());
    return usedLabels.peek();
  }

  public Integer useLabel(){
    return usedLabels.pollLast();
  }
}