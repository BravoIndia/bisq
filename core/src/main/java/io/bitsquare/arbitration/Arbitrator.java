/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.arbitration;

import io.bitsquare.locale.LanguageUtil;
import io.bitsquare.storage.Storage;
import io.bitsquare.user.User;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;

import java.io.Serializable;

import java.security.PublicKey;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class Arbitrator implements Serializable {
    private static final long serialVersionUID = 1L;


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Enums
    ///////////////////////////////////////////////////////////////////////////////////////////

    public enum ID_TYPE {
        REAL_LIFE_ID,
        NICKNAME,
        COMPANY
    }

    public enum METHOD {
        TLS_NOTARY,
        SKYPE_SCREEN_SHARING,
        SMART_PHONE_VIDEO_CHAT,
        REQUIRE_REAL_ID,
        BANK_STATEMENT,
        OTHER
    }

    public enum ID_VERIFICATION {
        PASSPORT,
        GOV_ID,
        UTILITY_BILLS,
        FACEBOOK,
        GOOGLE_PLUS,
        TWITTER,
        PGP,
        BTC_OTC,
        OTHER
    }


    transient private Storage<Arbitrator> storage;
    transient private boolean saveOnEveryUpdate;

    // Persisted fields
    private String id;
    private byte[] pubKey;
    private PublicKey p2pSigPubKey;
    private String name;
    private Reputation reputation;

    // editable
    private ID_TYPE idType;
    private List<Locale> languages;
    private Coin fee;
    private List<METHOD> arbitrationMethods;
    private List<ID_VERIFICATION> idVerifications;
    private String webUrl;
    private String description;


    @Inject
    public Arbitrator(Storage<Arbitrator> storage, User user) {
        this.storage = storage;

        Arbitrator persisted = storage.initAndGetPersisted(this);
        if (persisted != null) {
            //TODO for mock arbitrator
            id = persisted.getName();

            this.pubKey = persisted.getPubKey();
            this.p2pSigPubKey = persisted.getP2pSigPubKey();
            this.name = persisted.getName();
            this.idType = persisted.getIdType();
            this.languages = persisted.getLanguages();
            this.reputation = persisted.getReputation();
            this.fee = persisted.getFee();
            this.arbitrationMethods = persisted.getArbitrationMethods();
            this.idVerifications = persisted.getIdVerifications();
            this.webUrl = persisted.getWebUrl();
            this.description = persisted.getDescription();
        }
        else {
            // Mock
            id = "Manfred Karrer";
            this.pubKey = new ECKey().getPubKey();
            this.p2pSigPubKey = user.getP2PSigPubKey();
            this.name = "Manfred Karrer";
            this.idType = Arbitrator.ID_TYPE.REAL_LIFE_ID;
            this.languages = Arrays.asList(LanguageUtil.getDefaultLanguageLocale());
            this.reputation = new Reputation();
            this.fee = Coin.parseCoin("0.1");
            this.arbitrationMethods = Arrays.asList(Arbitrator.METHOD.TLS_NOTARY);
            this.idVerifications = Arrays.asList(ID_VERIFICATION.PASSPORT);
            this.webUrl = "https://bitsquare.io";
            this.description = "Bla bla...";
            doSave();
        }
    }

    public void save() {
        if (saveOnEveryUpdate)
            doSave();
    }

    private void doSave() {
        storage.save();
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hashCode(id);
        }
        else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Arbitrator)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Arbitrator other = (Arbitrator) obj;
        return id != null && id.equals(other.getId());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void setSaveOnEveryUpdate(boolean saveOnEveryUpdate) {
        this.saveOnEveryUpdate = saveOnEveryUpdate;
    }

    public void setDescription(String description) {
        this.description = description;
        doSave();
    }

    public void setIdType(ID_TYPE idType) {
        this.idType = idType;
        doSave();
    }

    public void setLanguages(List<Locale> languages) {
        this.languages = languages;
        doSave();
    }

    public void setFee(Coin fee) {
        this.fee = fee;
        doSave();
    }

    public void setArbitrationMethods(List<METHOD> arbitrationMethods) {
        this.arbitrationMethods = arbitrationMethods;
        doSave();
    }

    public void setIdVerifications(List<ID_VERIFICATION> idVerifications) {
        this.idVerifications = idVerifications;
        doSave();
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
        doSave();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////////////////////

    public String getId() {
        return id;
    }

    public byte[] getPubKey() {
        return pubKey;
    }

    public PublicKey getP2pSigPubKey() {
        return p2pSigPubKey;
    }

    public String getName() {
        return name;
    }

    public ID_TYPE getIdType() {
        return idType;
    }

    public List<Locale> getLanguages() {
        return languages;
    }

    public Reputation getReputation() {
        return reputation;
    }

    public Coin getFee() {
        return fee;
    }

    public List<METHOD> getArbitrationMethods() {
        return arbitrationMethods;
    }

    public List<ID_VERIFICATION> getIdVerifications() {
        return idVerifications;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getDescription() {
        return description;
    }
}