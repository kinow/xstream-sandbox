/*
 * The MIT License
 *
 * Copyright (c) <2012> <Bruno P. Kinoshita>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.eti.kinoshita.xstream;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;




/**
 * 
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 0.1
 */
public class Bird {

    private final Object beak; // yup, the beak never changes 
    private Object feathers;

    /**
     * @param beak
     * @param feathers
     */
    public Bird(Object beak, Object feathers) {
        super();
        this.beak = beak;
        this.feathers = feathers;
    }
    
    /**
     * @return the feathers
     */
    public Object getFeathers() {
        return feathers;
    }
    
    /**
     * @param feathers the feathers to set
     */
    public void setFeathers(Object feathers) {
        this.feathers = feathers;
    }
    
    /**
     * @return the beak
     */
    public Object getBeak() {
        return beak;
    }
    
    public static void main(String[] args) throws Exception {
        XStream xs = new XStream();
        File f = new File("/tmp/bird2.xml");
        xs.registerConverter(new Converter() {
            
            public boolean canConvert(Class type) {
                return type.equals(Bird.class);
            }
            
            public Object unmarshal(HierarchicalStreamReader reader,
                                    UnmarshallingContext context) {
                Object beak = null;
                Object feathers = null;
                while(reader.hasMoreChildren()) {
                    reader.moveDown();
                    if(reader.getNodeName().equals("beak")) {
                        beak = reader.getValue();
                    }
                    if(reader.getNodeName().equals("feathers")) {
                        feathers = reader.getValue();
                    }
                }
                Bird bird = new Bird(beak, feathers);
                return bird;
            }
            
            public void marshal(Object source, HierarchicalStreamWriter writer,
                                MarshallingContext context) {
                Bird bird = (Bird)source;
                writer.startNode("beak");
                writer.setValue(bird.getBeak() == null ? "" : bird.getBeak().toString());
                writer.endNode();
                writer.startNode("feathers");
                writer.setValue(bird.getFeathers() == null ? "" : bird.getFeathers().toString());
                writer.endNode();
            }
        });
        Bird bird = new Bird(new Object(), new String("many"));
        String s = xs.toXML(bird);
        System.out.println(s);
        FileUtils.write(f, s);
        Bird anotherBird = (Bird) xs.fromXML(f);
        System.out.println(anotherBird);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Bird [beak=" + beak + ", feathers=" + feathers + "]";
    }

}
