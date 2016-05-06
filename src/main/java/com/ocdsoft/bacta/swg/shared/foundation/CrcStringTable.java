package com.ocdsoft.bacta.swg.shared.foundation;

import bacta.iff.Iff;
import com.ocdsoft.bacta.soe.util.SOECRC32;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

import static com.ocdsoft.bacta.swg.shared.foundation.Tag.TAG_0000;
import static com.ocdsoft.bacta.swg.shared.foundation.Tag.TAG_DATA;

/**
 * Created by crush on 11/22/2015.
 */
public class CrcStringTable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrcStringTable.class);

    public static final int TAG_CSTB = Tag.convertStringToTag("CSTB");
    public static final int TAG_CRCT = Tag.convertStringToTag("CRCT");
    public static final int TAG_STNG = Tag.convertStringToTag("STNG");
    public static final int TAG_STRT = Tag.convertStringToTag("STRT");

    private TIntObjectMap<String> strings;

    public CrcStringTable() {
    }

    public CrcStringTable(final Iff iff) {
        load(iff);
    }

    public void load(final Iff iff) {
        iff.enterForm(TAG_CSTB);
        {
            final int version = iff.getCurrentName();

            if (version == TAG_0000)
                load0000(iff);
            else
                LOGGER.error("Unknown crc string table version in {}", iff.getFileName());
        }
        iff.exitForm(TAG_CSTB);
    }

    public ConstCharCrcString lookUp(final String string) {
        return lookUp(Crc.calculate(string));
    }

    public ConstCharCrcString lookUp(final int crc) {
        final String string = strings.get(crc);

        if (string != null) {
            return new ConstCharCrcString(string, crc);
        }

        return ConstCharCrcString.EMPTY;
    }

    public Collection<String> getAllStrings() {
        return Collections.unmodifiableCollection(strings.valueCollection());
    }

    public int getNumberOfStrings() {
        return strings.size();
    }

    public ConstCharCrcString getString(int index) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    private void load0000(final Iff iff) {
        iff.enterForm(TAG_0000);
        {
            final int numberOfEntries;

            iff.enterChunk(TAG_DATA);
            {
                numberOfEntries = iff.readInt();
            }

            iff.exitChunk(TAG_DATA);

            //Read string crcs. Don't need this.
            iff.enterChunk(TAG_CRCT);
            {
                for (int i = 0; i < numberOfEntries; ++i)
                    iff.readInt();
            }

            iff.exitChunk(TAG_CRCT);

            //Read string offsets. Don't need this.
            iff.enterChunk(TAG_STRT);
            {
                for (int i = 0; i < numberOfEntries; ++i)
                    iff.readInt();
            }
            iff.exitChunk(TAG_STNG);

            iff.enterChunk(TAG_STNG);
            {
                strings = new TIntObjectHashMap<>(numberOfEntries);

                for (int i = 0; i < numberOfEntries; ++i) {
                    final String string = iff.readString();
                    final int crc = SOECRC32.hashCode(string);

                    strings.put(crc, string);
                }
            }
            iff.exitChunk(TAG_STNG);
        }
        iff.exitForm(TAG_0000);
    }
}
